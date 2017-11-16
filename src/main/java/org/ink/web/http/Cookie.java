package org.ink.web.http;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author zhuyichen
 */
public class Cookie {

    private String name;
    private String value;
    private String host;
    private String path;
    private String domain;
    private long createTime = System.currentTimeMillis();
    private long expiredTime;
    private boolean httpOnly;
    private boolean secure;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
        //two hours
        this.expiredTime = createTime + 7200000;

        this.httpOnly = true;
        this.secure = false;
    }

    public Cookie(io.netty.handler.codec.http.cookie.Cookie cookie) {
        this.name = cookie.name();
        this.value = cookie.value();
        this.domain = cookie.domain();
        this.path = cookie.path();
        this.httpOnly = cookie.isHttpOnly();
        this.secure = cookie.isSecure();
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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(":")
                .append(getValue());
        if (httpOnly) {
            builder.append("; HttpOnly");
        }
        if (secure) {
            builder.append("; Secure");
        }

        return builder.toString();
    }
}
