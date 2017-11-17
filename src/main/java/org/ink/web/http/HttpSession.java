package org.ink.web.http;

import io.netty.channel.Channel;
import org.ink.security.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuyichen 2017-8-15
 */
public class HttpSession {

    /*
     * session id
     */

    private String sessionId;

    /*
     * for security
     */

    private User user;

    /**
     * for personal attributes, lazy initialization
     */
    private Map<String, Object> attributes;

    private Channel channel;

    private long createTime = System.currentTimeMillis();
    private long maxAge = 2*60*60;

    public HttpSession() {

    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public long maxAge() {
        return maxAge;
    }

    /**
     * judge current session has expires
     */
    public boolean hasExpires() {
        return createTime + TimeUnit.SECONDS.toMillis(maxAge) <= System.currentTimeMillis();
    }

    public Channel channel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String sessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User user() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    /**
     * lazy init attributes
     */
    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

}
