package org.ink.aop;

import org.ink.aop.annotation.After;
import org.ink.aop.annotation.Before;
import org.ink.web.http.Request;
import org.ink.web.http.Response;
import org.ink.web.route.Route;

import java.lang.reflect.Method;

//在ProxyChain中的Node
public class ProxyEntity {

    //方法
    private Method proxyMethod;

    //方法所在的对象
    private Object target;

    //参数
    private Object[] objects;


    public ProxyEntity(Method proxyMethod, Object target) {
        this.proxyMethod = proxyMethod;
        this.target = target;
        int i = proxyMethod.getParameterCount();
        objects = new Object[i];
    }

    public boolean doAction(Request request, Response response, Route route) {
        proxyMethod.setAccessible(true);
        try {
            AopUtil.argsSetter(this,  route, request, response);
            Object result = proxyMethod.invoke(target, objects);
            if (result == null) {
                return true;
            }
            else {
                return (Boolean)result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return true;
        }
    }

    public Method getProxyMethod() {
        return proxyMethod;
    }

    public void setProxyMethod(Method proxyMethod) {
        this.proxyMethod = proxyMethod;
    }

    public Object getObject() {
        return target;
    }

    public void setObject(Object object) {
        this.target = object;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (proxyMethod.getAnnotation(Before.class) != null) {
            builder.append("before " + proxyMethod.getAnnotation(Before.class).value() + " ");
        }
        if (proxyMethod.getAnnotation(After.class) != null) {
            builder.append("After " + proxyMethod.getAnnotation(After.class).value());
        }
        return builder.toString();
    }
}
