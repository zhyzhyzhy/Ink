package org.ink.security;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.ink.WebConfig;
import org.ink.db.annotation.Service;
import org.ink.ioc.bean.BeanDefinition;
import org.ink.ioc.IocContext;
import org.ink.security.annotation.UserDetail;
import org.ink.security.user.User;
import org.ink.security.user.UserDetailService;
import org.ink.web.WebContext;
import org.ink.web.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SecurityManager {

    private static List<UserDetailService> roleDetails = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(SecurityManager.class);

    public static void configure(IocContext iocContext, Class<?> configureclass) {
        log.info("starting configure securityService...");

        Map<String, BeanDefinition> beanDefinitionMap = iocContext.getDefinitions();
        //保存所有的service
        for (String key : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            Class<?> clazz = beanDefinition.getObject().getClass();
            if (clazz.getAnnotation(Service.class) != null) {
                Class superclass = clazz.getSuperclass();
                //如果实现了UserDetailService接口的，就作为一个登陆选项
                if (superclass.equals(UserDetailService.class)) {
                    roleDetails.add((UserDetailService) beanDefinition.getObject());
                }
            }
        }

        if (WebConfig.SECURITY_KEY != null) {
            iocContext.registerBean(new AuthenticationRoutes());
            SecurityConfig.KEY = WebConfig.SECURITY_KEY;
            SecurityConfig.anthenticationOpen = true;
        }


    }

    static User login(String userName, String password) {

        User currentUser = null;
        for (UserDetailService roleDetails : roleDetails) {
            currentUser = roleDetails.loadUser(userName);
            if (currentUser != null && currentUser.getPassword().equals(password)) {
                return currentUser;
            }
        }
        return currentUser;
    }

    public static CheckResult check(Route route) {
        if (WebContext.currentSession().user() == null) {
            return new CheckResult(HttpResponseStatus.UNAUTHORIZED);
        }
        User user = WebContext.currentSession().user();
        if (route.containsRolesAll(user.getRoles())) {
            return new CheckResult(true);
        }
        else {
            return new CheckResult(HttpResponseStatus.FORBIDDEN);
        }

    }

    static void UserDetailSetter(Route route, User user) {
        Parameter[] parameters = route.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameters[i].getAnnotations()) {
                if (annotation instanceof UserDetail) {
                    route.getParamters()[i] = user;
                }
            }
        }
    }

}

