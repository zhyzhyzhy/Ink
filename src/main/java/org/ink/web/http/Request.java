package org.ink.web.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class Request {

    private static Logger logger = LoggerFactory.getLogger(Request.class);

    private FullHttpRequest fullHttpRequest;

    private Channel channel;

    private String sessionId;

    private HttpSession session;

    private Map<String, Cookie> cookies = new HashMap<>();

    public Request(Channel channel, FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        this.channel = channel;
        //得到cookie
        parseCookies();
        //得到session
        parseSession();
    }

    private void parseCookies() {
        //use ServerCookieDecoder to decode cookies in headers
        if (fullHttpRequest.headers().contains("Cookie")) {
            ServerCookieDecoder.LAX.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(cookie -> cookies.putIfAbsent(cookie.name(), new Cookie(cookie)));
        }
    }

    private void parseSession() {
        Cookie sessionId = cookies.getOrDefault("SessionId", null);
        if (sessionId != null) {
            logger.debug(sessionId.getValue());
            session = SessionManager.getSession(sessionId.getValue());
        }
    }


    public Map<String, Cookie> cookies() {
        return cookies;
    }

    public String getUri() {
        return fullHttpRequest.uri();
    }


    public HttpSession getSession() {
        return session;
    }

}
