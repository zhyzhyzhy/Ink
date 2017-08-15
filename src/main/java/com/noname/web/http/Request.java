package com.noname.web.http;

import com.noname.web.http.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.CookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class Request {

    private FullHttpRequest fullHttpRequest;

    private HttpSession session;

    private Map<String, String> cookies = new HashMap<>();

    public Request(Channel channel, FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        //得到cookie
        parseCookies();
        //得到session
        parseSession(channel);
    }

    private void parseCookies() {
        //use ServerCookieDecoder to decode cookies in headers
        if (fullHttpRequest.headers().contains("Cookie")) {
            ServerCookieDecoder.LAX.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(header -> cookies.putIfAbsent(header.name(), header.value()));
        }
    }

    private void parseSession(Channel channel) {
        String sessionId = cookies.getOrDefault("SessionId", null);
        if (sessionId == null) {
            return;
        }
        this.session = SessionUtil.getSession(channel);
    }



    public Map<String, String> cookies() {
        return cookies;
    }

    public String getUri() {
        return fullHttpRequest.uri();
    }


    public HttpSession getSession() {
        return session;
    }

}
