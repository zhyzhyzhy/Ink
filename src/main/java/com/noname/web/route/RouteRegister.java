package com.noname.web.route;

import com.noname.ioc.bean.BeanDefinition;
import com.noname.web.annotation.DELETE;
import com.noname.web.annotation.GET;
import com.noname.web.annotation.POST;
import com.noname.web.annotation.PUT;
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
 *
 * 登记路由信息
 */
public class RouteRegister {

    private static final Logger log = LoggerFactory.getLogger(RouteRegister.class);

    public static List<Route> registerRoute(Map<String, BeanDefinition> beanDefinitionMap) {

        log.info("starting configure route...");

        //保存所有的路由
        List<Route> routes = new ArrayList<>();

        for (String key : beanDefinitionMap.keySet()) {

            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            Method[] methods = beanDefinition.getBeanClassName().getDeclaredMethods();

            for (Method method : methods) {

                Annotation[] annotations = method.getDeclaredAnnotations();

                for (Annotation annotation : annotations) {

                    method.setAccessible(true);

                    //没想到优化方法。。。暂时先这样了
                    Route route = null;
                    String path = "";

                    if (annotation instanceof GET) {
                        route = new Route(beanDefinition.getObject(), method, HttpMethod.GET, ((GET) annotation).value());
                        path = ((GET) annotation).value();
                    }
                    else if(annotation instanceof POST) {
                        route = new Route(beanDefinition.getObject(), method, HttpMethod.POST, ((POST) annotation).value());
                        path = ((POST) annotation).value();
                    }
                    else if(annotation instanceof PUT) {
                        route = new Route(beanDefinition.getObject(), method, HttpMethod.PUT, ((PUT) annotation).value());
                        path = ((PUT) annotation).value();
                    }
                    else if(annotation instanceof DELETE) {
                        route = new Route(beanDefinition.getObject(), method, HttpMethod.DELETE, ((DELETE) annotation).value());
                        path = ((DELETE) annotation).value();
                    }

                    if (route == null) {
                        continue;
                    }

                    //如果已经有这个路由
                    if (routes.contains(route)) {
                        log.error("route {} has contained", route.getPath());
                    }
                    else {
                        routes.add(route);
                        route.setParamters(new Object[method.getParameterCount()]);
                        RouteFinder.addRouter(RouteFinder.pathCompiler(path, method), route);
                        log.info("mapping {} [{}]", route.getHttpMethod(), route.getPath());
                    }
                }
            }
        }
        return routes;
    }

}
