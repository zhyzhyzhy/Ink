package org.ink.web.route;

import org.ink.ioc.bean.BeanDefinition;
import org.ink.security.annotation.Role;
import org.ink.web.annotation.DELETE;
import org.ink.web.annotation.GET;
import org.ink.web.annotation.POST;
import org.ink.web.annotation.PUT;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
            Method[] methods = beanDefinition.getClazz().getDeclaredMethods();

            for (Method method : methods) {

                Annotation[] annotations = method.getDeclaredAnnotations();

                Route route = null;
                String path = "";
                boolean isSecurity = false;
                for (Annotation annotation : annotations) {

                    method.setAccessible(true);

                    //没想到优化方法。。。暂时先这样了

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


                    if (annotation instanceof Role) {
                        isSecurity = true;
                    }

                }


                if (route == null) {
                    continue;
                }

                //如果已经有这个路由
                if (routes.contains(route)) {
                    log.error("route {} has contained", route.getPath());
                }
                else {

                    route.setSecurity(isSecurity);

                    routes.add(route);
                    route.setParamters(new Object[method.getParameterCount()]);
                    RouteFinder.addRouter(RouteFinder.pathCompiler(path, method), route);
                    if (route.isSecurity()) {
                        log.info("mapping {} [{}] with roles [{}]", route.getHttpMethod(), route.getPath(), method.getAnnotation(Role.class).value());
                    }
                    else {
                        log.info("mapping {} [{}] with roles [all]", route.getHttpMethod(), route.getPath());
                    }
                }
            }
        }
        return routes;
    }



}
