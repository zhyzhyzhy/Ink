package com.noname.aop;

import com.noname.aop.annotation.After;
import com.noname.aop.annotation.Before;

import java.lang.reflect.Method;

//在ProxyChain中的Node
public class ProxyEntity {

    //方法
    private Method proxyMethod;
    //方法所在的对象
    private Object object;

    //参数
    private Object[] objects;

    public ProxyEntity(Method proxyMethod, Object object) {
        this.proxyMethod = proxyMethod;
        this.object = object;
    }

    public void doAction(Object[] objects) {
        proxyMethod.setAccessible(true);
        try {
            proxyMethod.invoke(object, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Method getProxyMethod() {
        return proxyMethod;
    }

    public void setProxyMethod(Method proxyMethod) {
        this.proxyMethod = proxyMethod;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
