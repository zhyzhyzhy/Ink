package org.ink.web.http;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

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
    private long maxAge = -1;
    private boolean httpOnly;
    private boolean secure;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
        //two hours
        maxAge = 2*60*60;
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
        this.maxAge = cookie.maxAge();
    }


    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String value() {
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

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public long maxAge() {
        return maxAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cookie cookie = (Cookie) o;

        return new EqualsBuilder()
                .append(createTime, cookie.createTime)
                .append(maxAge, cookie.maxAge)
                .append(httpOnly, cookie.httpOnly)
                .append(secure, cookie.secure)
                .append(name, cookie.name)
                .append(value, cookie.value)
                .append(host, cookie.host)
                .append(path, cookie.path)
                .append(domain, cookie.domain)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(value)
                .append(host)
                .append(path)
                .append(domain)
                .append(createTime)
                .append(maxAge)
                .append(httpOnly)
                .append(secure)
                .toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name())
                .append("=")
                .append(value());
        if (maxAge != -1) {
            builder.append("; Max-Age")
                    .append("=")
                    .append(maxAge());
        }
        if (httpOnly) {
            builder.append("; HttpOnly");
        }
        if (secure) {
            builder.append("; Secure");
        }

        return builder.toString();
    }

    public static Set<Cookie> decode(String head) {
        if (head == null) {
            return null;
        }
        Set<Cookie> cookies = new HashSet<>();
        String[] pairs = head.split(";");

        for (String s : pairs) {
            String[] kv = s.split("=");
            if (kv.length != 2) {
                continue;
            }

            //get name
            String name = kv[0];
            int nameStart = -1;
            int nameEnd = -1;
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == ' ') {
                    continue;
                }
                else {
                    nameStart = i;
                    break;
                }
            }
            for (int i = name.length() - 1; i >= 0; i--) {
                if (name.charAt(i) != ' ') {
                    nameEnd = i + 1;
                    break;
                }
            }
            if (nameStart == -1 || nameEnd == -1 || nameEnd <= nameStart) {
                continue;
            }

            String value = kv[1];
            int valueStart = -1;
            int valueEnd = -1;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == ' ') {
                    continue;
                }
                else {
                    valueStart = i;
                    break;
                }
            }
            for (int i = value.length() - 1; i >= 0; i--) {
                if (value.charAt(i) != ' ') {
                    valueEnd = i + 1;
                    break;
                }
            }

            if (valueStart == -1 || valueEnd == -1 || valueEnd <= valueStart) {
                continue;
            }

            Cookie cookie = new Cookie(name.substring(nameStart, nameEnd), value.substring(valueStart, valueEnd));
            cookies.add(cookie);
            System.out.println(cookie);
        }

        return cookies;
    }
}
