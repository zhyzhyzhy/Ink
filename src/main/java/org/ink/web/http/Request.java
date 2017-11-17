package org.ink.web.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.ink.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuyichen on 2017/7/12.
 */
public class Request {

    private static Logger logger = LoggerFactory.getLogger(Request.class);

    private FullHttpRequest fullHttpRequest;

    private HttpMethod method;

    private Channel channel;

    //key -> value
    private Map<String, String> cookies = new HashMap<>();

    public Request() {}
    public Request(Channel channel, FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        this.channel = channel;
        this.method = fullHttpRequest.method();
        //get cookie
        parseCookie();
    }

    private void parseCookie() {
        //use ServerCookieDecoder to decode cookies in headers
        if (fullHttpRequest.headers().contains("Cookie")) {

            Cookie.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(cookie -> {
                        cookies.putIfAbsent(cookie.name(), cookie.value());
                    });
        }

    }


    public HttpMethod method() {
        return method;
    }

    public Channel channel() {
        return channel;
    }

    public Map<String, String> cookies() {
        return cookies;
    }

    public boolean containsCookie(String name) {
        return cookies.containsKey(name);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    public String uri() {
        return fullHttpRequest.uri();
    }

}
