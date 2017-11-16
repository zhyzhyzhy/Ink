package org.ink.web.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
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

    private Channel channel;

    private HttpSession session;

    //key -> Cookie
    private Map<String, Cookie> cookies = new HashMap<>();

    public Request(Channel channel, FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        this.channel = channel;
        //get cookie and set session
        parseSession();
    }

    private void parseSession() {
        //use ServerCookieDecoder to decode cookies in headers
        if (fullHttpRequest.headers().contains("Cookie")) {
            ServerCookieDecoder.STRICT.decode(fullHttpRequest.headers().get("Cookie"))
                    .forEach(cookie -> cookies.putIfAbsent(cookie.name(), new Cookie(cookie.name(), cookie.value())));
        }

        Cookie sessionId = cookies.getOrDefault("sessionid", null);

        if (sessionId == null) {
            String sessionid = SessionManager.createSessionId();
            SessionManager.addSession(sessionid, channel);
            session = SessionManager.getSession(sessionid);
        }
        else {
            session = SessionManager.getSession(sessionId.getValue());
        }
    }


    public Map<String, Cookie> cookies() {
        return cookies;
    }

    public String uri() {
        return fullHttpRequest.uri();
    }


    public HttpSession getSession() {
        return session;
    }

}
