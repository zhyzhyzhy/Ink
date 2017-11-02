package com.noname.aop;

import com.noname.aop.annotation.After;
import com.noname.aop.annotation.Before;
import com.noname.aop.annotation.Proxy;
import com.noname.ioc.bean.BeanDefinition;
import com.noname.web.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ProxyManager {

    private static final Logger log = LoggerFactory.getLogger(ProxyManager.class);

    private static Map<Pattern, ProxyEntity> beforeMap = new HashMap<>();
    private static Map<Pattern, ProxyEntity> afterMap = new HashMap<>();

    public static void registerProxy(Map<String, BeanDefinition> map, List<Route> routes) {
        Collection<BeanDefinition> beanDefinitions = map.values();

        beanDefinitions.stream()
                .filter(bd -> bd.getBeanClassName().getAnnotation(Proxy.class) != null)
                .forEach(bd -> {
                    //get all methods
                    Method[] methods = bd.getBeanClassName().getMethods();
                    for (Method method : methods) {
                        // handle Before Aop
                        if (method.getAnnotation(Before.class) != null) {
                            String url = method.getAnnotation(Before.class).value();
                            ProxyEntity proxyEntity = new ProxyEntity(method, bd.getObject());
                            Pattern pattern = Pattern.compile(url);
                            beforeMap.put(pattern, proxyEntity);
                            log.info("put beforeAop {} ", proxyEntity);
                        }
                        //handle After Aop
                        if (method.getAnnotation(After.class) != null) {
                            String url = method.getAnnotation(After.class).value();
                            ProxyEntity proxyEntity = new ProxyEntity(method, bd.getObject());
                            Pattern pattern = Pattern.compile(url);
                            afterMap.put(pattern, proxyEntity);
                            log.info("put afterAop {} ", proxyEntity);
                        }
                    }
                });

        registerProxyChains(routes);

    }

    public static void registerProxyChains(List<Route> routes) {
        routes.forEach(route -> {
            beforeMap.forEach((pattern, proxyEntity) -> {
                if (pattern.matcher(route.getPath()).matches()) {
                    route.getBeforeProxyChain().addProxyEntity(proxyEntity);
                    log.info("add beforeAop {} to route {}", proxyEntity, route);
                }
            });

            afterMap.forEach((pattern, proxyEntity) -> {
                if (pattern.matcher(route.getPath()).matches()) {
                    route.getAfterProxyChain().addProxyEntity(proxyEntity);
                    log.info("add afterAop {} to route {}", proxyEntity, route);
                }
            });
        });
    }
}
