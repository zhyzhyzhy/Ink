package com.noname.web.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class Request {

    private FullHttpRequest fullHttpRequest;

    private String sessionId;

    private Map<String, String> cookies = new HashMap<>();

    public Request(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        //得到cookie
        parseCookies();
        //得到session
        parseSession();
    }

    private void parseCookies() {
        //use ServerCookieDecoder to decode cookies in headers
        if (fullHttpRequest.headers().contains("Cookie")) {
            ServerCookieDecoder.LAX.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(header -> cookies.putIfAbsent(header.name(), header.value()));
        }
    }

    private void parseSession() {
        String sessionId = cookies.getOrDefault("SessionId", null);
    }



    public Map<String, String> cookies() {
        return cookies;
    }

    public String getUri() {
        return fullHttpRequest.uri();
    }


    public HttpSession getSession() {
        if (sessionId == null) {
            return null;
        }
        return SessionManager.getSession(sessionId);
    }

}
