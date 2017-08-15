package com.noname.web.http;

import com.noname.security.User;
import com.noname.web.http.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/8/15.
 */
public class HttpSession {

    //对应的channel
    private Channel channel;

    //sessionId
    private String sessionId;

    //对应的身份验证，如果无就是null
    private User user;

    //for personal attributes, lazy initialization
    private Map<String, Object> attributes;

    public HttpSession(Channel channel) {
        this.channel = channel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

}
