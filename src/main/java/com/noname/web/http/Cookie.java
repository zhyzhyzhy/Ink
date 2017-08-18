package com.noname.web.http;

import java.util.Date;

/**
 * Created by zhuyichen on 2017/8/18.
 */
public class Cookie {

    private String name;
    private String value;
    private String host;
    private String path;
    private long createTime = new Date().getTime();
    private long expiredTime;
    private boolean httpOnly;
    private boolean isSecure;

    public Cookie(io.netty.handler.codec.http.cookie.Cookie cookie) {
        this.name = cookie.name();
        this.value = cookie.value();
        this.host = cookie.domain();
        this.path = cookie.path();
        this.httpOnly = cookie.isHttpOnly();
        this.isSecure = cookie.isSecure();
        this.expiredTime = cookie.maxAge();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
}
