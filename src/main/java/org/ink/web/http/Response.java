package org.ink.web.http;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ink.web.WebContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Response Builder
 * 建造者模式
 *
 * @author zhuyichen 2017-7-17
 */
public class Response {

    /**
     * the response body
     */
    private Object body;

    /**
     * the response http status
     * the default is not found
     */
    private HttpResponseStatus responseStatus = HttpResponseStatus.NOT_FOUND;

    /**
     * the headers of the response
     * except set-cookie header
     * because set-cookie can set many times, so i set it in {@code Set<Cookies>}
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * the cookies
     * lazy init
     */
    private Set<Cookie> cookies;

    /**
     * if need to transfer file
     * lazy init
     */
    private RandomAccessFile file;

    public RandomAccessFile file() {
        return file;
    }

    public Response() {

    }

    public Response(Request request) {
        if (request.cookies() != null) {
            String sessionId = request.cookies().getOrDefault("SESSIONID", null);
            HttpSession session = null;
            //if sessionid is null or Manager not contains sessionid
            //then create one new http session
            if (sessionId == null || !SessionManager.containsSession(sessionId)) {
                sessionId = SessionManager.createSessionId();
                SessionManager.addSession(sessionId, request.channel());
                session = SessionManager.getSession(sessionId);
                addCookie("SESSIONID", sessionId);
            } else {
                session = SessionManager.getSession(sessionId);
                //if session hasExpired
                if (session.hasExpires()) {
                    sessionId = SessionManager.createSessionId();
                    SessionManager.addSession(sessionId, request.channel());
                    session = SessionManager.getSession(sessionId);
                    addCookie("SESSIONID", sessionId);
                }
            }
            //set to WebContext
            WebContext.setCurrentSession(session);
        }
    }

    /**
     * add one new cookie in response
     */
    public boolean addCookie(String name, String value) {
        if (cookies == null) {
            cookies = new HashSet<>();
        }
        return cookies.add(new Cookie(name, value));
    }

    public boolean addCookie(Cookie cookie) {
        return cookies.add(cookie);
    }

    /**
     * get all cookies
     */
    public Set<Cookie> cookies() {
        return cookies;
    }

    /**
     * @return all current headers in the response
     * except set-cookie
     */
    public Map<String, String> headers() {
        return headers;
    }

    /**
     * add one new header
     */
    public void header(String header, String value) {
        headers.putIfAbsent(header, value);
    }


    private Response(Object body, HttpResponseStatus responseStatus, Map<String, String> headers, Set<Cookie> cookies) {
        this.body = body;
        this.responseStatus = responseStatus;
        this.headers = headers;
        this.cookies = cookies;
    }

    private Response(Object body, HttpResponseStatus responseStatus, Map<String, String> headers, Set<Cookie> cookies, RandomAccessFile file) {
        this.body = body;
        this.responseStatus = responseStatus;
        this.headers = headers;
        this.cookies = cookies;
        this.file = file;
    }


    /**
     * get and set response body
     */
    public Object body() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    /**
     * get and set response http status
     *
     * @return
     */
    public HttpResponseStatus responseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }


    /**
     * the builder of the response
     */
    public static Builder ok() {
        return status(HttpResponseStatus.OK);
    }

    public static Builder badRequest() {
        return status(HttpResponseStatus.BAD_REQUEST);
    }


    public static Builder status(HttpResponseStatus status) {
        return new Builder(status);
    }

    public static class Builder {
        private HttpResponseStatus status = HttpResponseStatus.OK;
        private Object body;
        private Map<String, String> headers;
        private Set<Cookie> cookies;
        private RandomAccessFile file;

        public Builder(HttpResponseStatus status) {
            this.status = status;
        }

        public Builder body(Object o) {
            this.body = o;
            return this;
        }

        public Response build() {
            Map<String, String> map = new HashMap<>();
            if (headers != null) {
                headers.keySet().forEach(s -> map.putIfAbsent(s, headers.get(s)));
            }
            if (file == null) {
                return new Response(body, status, map, cookies);
            }
            else {
                return new Response(body, status, map, cookies, file);
            }
        }

        public Builder header(String header, String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.putIfAbsent(header, value);
            return this;
        }

        public Builder cookie(String name, String value) {
            if (cookies == null) {
                cookies = new HashSet<>();
            }
            cookies.add(new Cookie(name, value));
            return this;
        }

        public Builder file(File file, String name) {
            if (this.file != null) {
                file = null;
            }
            try {
                this.file = new RandomAccessFile(file, "r");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Response response = (Response) o;

        return new EqualsBuilder()
                .append(body, response.body)
                .append(responseStatus, response.responseStatus)
                .append(headers, response.headers)
                .append(cookies, response.cookies)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(body)
                .append(responseStatus)
                .append(headers)
                .append(cookies)
                .toHashCode();
    }

    public static Response mergeResponse(Response response1, Response response2) {
        //TODO
        response1.headers.forEach(response2::header);
        return response2;
    }


    /**
     * build response like 404 304 without response body
     */
    public static DefaultFullHttpResponse buildDefaultFullHttpResponse(HttpResponseStatus status) {

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);

        Response currentResponse = WebContext.currentResponse();

        response.headers().add(HttpHeader.CONTENT_LENGTH, "0");
        if (currentResponse.cookies != null) {
            response.headers().add(HttpHeader.SET_COOKIE, currentResponse.cookies.stream()
                    .map(Cookie::toString)
                    .collect(Collectors.toList()));
        }
        for (String head : currentResponse.headers.keySet()) {
            response.headers().add(head, currentResponse.headers.get(head));
        }
        response.headers().add(HttpHeader.SERVER, "Ink");
        return response;
    }

    /**
     * convert response to DefaultFullHttpResponse
     */
    public HttpResponse buildDefaultFullHttpResponse() throws IOException {
        DefaultHttpResponse fullHttpResponse = null;

        int length = 0;
        //set body
        if (this.body() == null && this.file == null) {
            fullHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, this.responseStatus());
        } else {
            if (this.body() != null) {
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, this.responseStatus(), Unpooled.copiedBuffer(JSON.toJSONString(body()).getBytes()));
                length = response.content().readableBytes();
                fullHttpResponse = response;
            }
            else {
                //http file
                fullHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, this.responseStatus());

            }
        }

        //set headers
        for (String s : this.headers().keySet()) {
            fullHttpResponse.headers().add(s, headers.get(s));
        }

        //set content type if has not been done
        if (!fullHttpResponse.headers().contains("Content-type")) {
            if (body instanceof String) {
                fullHttpResponse.headers().set(HttpHeader.CONTENT_TYPE, "text/plain");
            } else {
                fullHttpResponse.headers().set(HttpHeader.CONTENT_TYPE, "application/json;charset=utf-8");
            }
        }

        //set set-cookie header
        if (cookies() != null) {
            fullHttpResponse.headers().add(HttpHeader.SET_COOKIE, cookies().stream()
                    .map(Cookie::toString)
                    .collect(Collectors.toList()));
        }
        fullHttpResponse.headers().set(HttpHeader.CONNECTION, "keep-alive");
        if (this.file != null) {
            fullHttpResponse.headers().set(HttpHeader.CONTENT_LENGTH, length + this.file.length());
        }
        else {
            fullHttpResponse.headers().set(HttpHeader.CONTENT_LENGTH, length);
        }
//        fullHttpResponse.headers().set(HttpHeader.SERVER, "Ink");
        return fullHttpResponse;
    }

    public static HttpResponse buildDefaultFullHttpResponse0() throws IOException{
        return WebContext.currentResponse().buildDefaultFullHttpResponse();
    }


}
