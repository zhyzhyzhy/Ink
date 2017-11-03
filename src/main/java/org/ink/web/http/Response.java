package org.ink.web.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/13.
 * <p>
 * Response Builder
 * 建造者模式
 */
public class Response {
    private Object responseEntity;
    private HttpResponseStatus responseStatus = HttpResponseStatus.NOT_FOUND;
    private Map<String, String> headers = new HashMap<>();

    public Response(){

    }

    public Response(Channel channel, FullHttpRequest fullHttpRequest) {
        final String[] sessionId = {null};
        if (fullHttpRequest.headers().contains("Cookie")) {
            ServerCookieDecoder.LAX.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(cookie -> {
                        if (cookie.name().equals("SessionId")) {
                            sessionId[0] = cookie.value();
                        }
                    });
        }
        if (sessionId[0] == null) {
            sessionId[0] = SessionManager.createSessionId();
            SessionManager.addSession(channel, sessionId[0]);
            headers.put(HttpHeader.SetCookie.toString(), "SessionId="+ sessionId[0]);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void header(HttpHeader header, String value) {
        headers.putIfAbsent(header.toString(), value);
    }

    public void header(String header, String value) {
        headers.putIfAbsent(header, value);
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    private Response(Object responseEntity, HttpResponseStatus responseStatus, Map<String, String> headers) {
        this.responseEntity = responseEntity;
        this.responseStatus = responseStatus;
        this.headers.putAll(headers);
    }



    public Object getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(Object responseEntity) {
        this.responseEntity = responseEntity;
    }

    public HttpResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

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
        private Object object;
        private Map<HttpHeader, String> headers;
        public Builder(HttpResponseStatus status) {
            this.status = status;
        }

        public Builder body(Object o) {
            this.object = o;
            return this;
        }
        public Response build() {
            Map<String, String> map = new HashMap<>();
            if (headers != null) {
                headers.keySet().forEach(s -> map.putIfAbsent(s.toString(), headers.get(s)));
            }
            return new Response(object, status, map);
        }
        public Builder header(HttpHeader header, String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.putIfAbsent(header, value);
            return this;
        }
    }

    public static Response mergeResponse(Response response1, Response response2) {
        //TODO

        response1.headers.forEach(response2::header);
        return response2;
    }


}
