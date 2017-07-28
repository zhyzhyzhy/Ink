package com.noname.web.route;


import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class Route {
    private Object object;
    private Method method;
    private HttpMethod httpMethod;
    private String path;

    private boolean security = false;

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }



    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getParamters() {
        return paramters;
    }

    public void setParamters(Object[] paramters) {
        this.paramters = paramters;
    }

    private Object[] paramters;

    public Route(Object object, Method method, HttpMethod httpMethod, String path) {
        this.object = object;
        this.method = method;
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (httpMethod != null ? !httpMethod.equals(route.httpMethod) : route.httpMethod != null) return false;
        return path != null ? path.equals(route.path) : route.path == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Router{" +
                "object=" + object +
                ", method=" + method +
                ", httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                '}';
    }
}
