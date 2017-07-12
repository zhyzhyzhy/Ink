package com.noname.web.route;

import com.noname.ioc.bean.BeanDefinition;
import com.noname.web.annotation.GET;
import com.noname.web.annotation.POST;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class RouteRegister {
    private static final Logger log = LoggerFactory.getLogger(RouteRegister.class);
    public static List<Route> registRoute(Map<String, BeanDefinition> beanDefinitionMap) {
        log.info("start configure router...");
        List<Route> routes = new ArrayList<>();
        for (String key : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            Method[] methods = beanDefinition.getBeanClassName().getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for ( Annotation annotation : annotations) {
                    method.setAccessible(true);
                    if (annotation instanceof GET) {
                        Route route = new Route(beanDefinition.getObject(), method, HttpMethod.GET,((GET) annotation).value());
                        if (routes.contains(route)) {
                            log.error("route {} has contained", route);
                        }
                        else {
                            routes.add(route);
                        }
                        log.info("register route {}", route);
                    }
                }
            }
        }
        return routes;
    }

}
