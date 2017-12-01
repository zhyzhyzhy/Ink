package org.ink.aop;

import org.ink.aop.annotation.After;
import org.ink.aop.annotation.Before;
import org.ink.aop.annotation.Proxy;
import org.ink.ioc.bean.BeanDefinition;
import org.ink.web.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * function registerProxy is looking for
 * class with annotation {@code @Proxy}
 * then looking for methods that has
 * annotation {@code @Before }, set it in {@code beforeMap}
 *
 * looking for methods that has annotation {@code After},
 * set it in {@code afterMap}
 *
 * all methods and its object is created into a ProxyEntity
 *
 * every route has one beforeAopChain and one afterAopChain
 * we need to set the methods in the target route with the rule
 * setted in annotation values
 *
 * @see org.ink.aop.annotation.After
 * @see org.ink.aop.annotation.Before
 * @see org.ink.aop.annotation.Proxy
 * @see org.ink.aop.ProxyEntity
 * @see org.ink.aop.ProxyChain
 * @author zhuyichen
 */
public class ProxyManager {

    private static final Logger log = LoggerFactory.getLogger(ProxyManager.class);

    /**
     * contains methods has annotation @Before
     */
    private static Map<Pattern, ProxyEntity> beforeMap = new HashMap<>();

    /**
     * contains methods has annotation @After
     */
    private static Map<Pattern, ProxyEntity> afterMap = new HashMap<>();


    /**
     * set all Aop methods in the maps
     * then set into target route ProxyChains
     * @param map the bean container in the IocContext
     * @param routes all routes in the project
     */
    public static void registerProxy(Map<String, BeanDefinition> map, List<Route> routes) {
        Collection<BeanDefinition> beanDefinitions = map.values();

        beanDefinitions.stream()
                .filter(bd -> bd.getClazz().getAnnotation(Proxy.class) != null)
                .forEach(bd -> {
                    //get all methods
                    Method[] methods = bd.getClazz().getMethods();
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

        //set all proxy methods in the target route
        registerProxyChains(routes);
    }

    /**
     * set all proxy methods in the target route
     * @param routes all routes in the project
     */
    private static void registerProxyChains(List<Route> routes) {
        routes.forEach(route -> {
            beforeMap.forEach((pattern, proxyEntity) -> {
                if (pattern.matcher(route.path()).matches()) {
                    route.beforeProxyChain().addProxyEntity(proxyEntity);
                    log.info("add beforeAop {} to route {}", proxyEntity, route);
                }
            });

            afterMap.forEach((pattern, proxyEntity) -> {
                if (pattern.matcher(route.path()).matches()) {
                    route.afterProxyChain().addProxyEntity(proxyEntity);
                    log.info("add afterAop {} to route {}", proxyEntity, route);
                }
            });
        });
    }
}
