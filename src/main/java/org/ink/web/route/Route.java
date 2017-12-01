package org.ink.web.route;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.ink.aop.ChainType;
import org.ink.aop.ProxyChain;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhuyichen 2017-7-12
 */
public class Route {

    //the object that method in
    private Object object;

    //the method
    private Method method;

    //the request method GET, POST, PUT, DELETE
    private HttpMethod httpMethod;

    //the request path
    private String path;

    //is has @Role annotation
    private boolean security = false;

    //if has @Role annotation, contains roles
    private Set<String> roles;

    public void addRolePermit(String roleName) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(roleName);
    }

    public boolean containsRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }

    public boolean containsRolesAll(List<String> roleNames) {
        return roles != null && roles.containsAll(roleNames);
    }

    //Aop的前置路由
    private ProxyChain beforeProxyChain = new ProxyChain(ChainType.BEFORE);
    //Aop的后置路由
    private ProxyChain afterProxyChain = new ProxyChain(ChainType.AFTER);

    public ProxyChain beforeProxyChain() {
        return beforeProxyChain;
    }

    public void setBeforeProxyChain(ProxyChain beforeProxyChain) {
        this.beforeProxyChain = beforeProxyChain;
    }

    public ProxyChain afterProxyChain() {
        return afterProxyChain;
    }

    public void setAfterProxyChain(ProxyChain afterProxyChain) {
        this.afterProxyChain = afterProxyChain;
    }

    public boolean security() {
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

    public HttpMethod httpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String path() {
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

        return new EqualsBuilder()
                .append(security, route.security)
                .append(object, route.object)
                .append(method, route.method)
                .append(httpMethod, route.httpMethod)
                .append(path, route.path)
                .append(beforeProxyChain, route.beforeProxyChain)
                .append(afterProxyChain, route.afterProxyChain)
                .append(paramters, route.paramters)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(object)
                .append(method)
                .append(httpMethod)
                .append(path)
                .append(security)
                .append(beforeProxyChain)
                .append(afterProxyChain)
                .append(paramters)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("object", object)
                .append("method", method)
                .append("httpMethod", httpMethod)
                .append("path", path)
                .append("security", security)
                .append("beforeProxyChain", beforeProxyChain)
                .append("afterProxyChain", afterProxyChain)
                .append("paramters", paramters)
                .toString();
    }
}
