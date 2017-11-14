package org.ink.aop;

import org.ink.aop.annotation.After;
import org.ink.aop.annotation.Before;
import org.ink.web.http.Request;
import org.ink.web.http.Response;
import org.ink.web.route.Route;

import java.lang.reflect.Method;

/**
 * the node in the ProxyChain
 * contains one proxy method and its object
 *
 * @author zhuyichen
 * @see org.ink.aop.ProxyChain
 */

public class ProxyEntity {

    /**
     * method of the entity
     */
    private Method proxyMethod;

    /**
     * the object
     */
    private Object methodObject;

    /**
     * the parameters of the methods
     */
    private Object[] parameters;


    ProxyEntity(Method proxyMethod, Object target) {
        this.proxyMethod = proxyMethod;
        this.methodObject = target;
        int i = proxyMethod.getParameterCount();
        parameters = new Object[i];
    }

    boolean doAction(Request request, Response response, Route route) {
        proxyMethod.setAccessible(true);
        try {
            AopKit.argsSetter(this, route, request, response);
            Object result = proxyMethod.invoke(methodObject, parameters);
            if (result == null) {
                return true;
            } else {
                return (Boolean) result;
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

    public Object[] getParameters() {
        return parameters;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (proxyMethod.getAnnotation(Before.class) != null) {
            builder.append("before ")
                    .append(proxyMethod.getAnnotation(Before.class).value())
                    .append(" ");
        }
        if (proxyMethod.getAnnotation(After.class) != null) {
            builder.append("After ")
                    .append(proxyMethod.getAnnotation(After.class).value())
                    .append(" ");
        }
        return builder.toString();
    }
}
