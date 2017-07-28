package com.noname.security;

import com.noname.db.Service;
import com.noname.ioc.bean.BeanDefinition;
import com.noname.security.annotation.Role;
import com.noname.security.annotation.UserDetail;
import com.noname.web.annotation.DELETE;
import com.noname.web.annotation.GET;
import com.noname.web.annotation.POST;
import com.noname.web.annotation.PUT;
import com.noname.web.route.Route;
import com.noname.web.route.RouteFinder;
import io.jsonwebtoken.Jwt;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SecurityManager {

    private static List<UserDetailService> roleDetails = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(SecurityManager.class);

    public static void getRoleServices(Map<String, BeanDefinition> beanDefinitionMap) {
        log.info("starting configure securityService...");

        //保存所有的service
        for (String key : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            if (beanDefinition.getObject().getClass().getAnnotation(Service.class) != null) {
                roleDetails.add((UserDetailService) beanDefinition.getObject());
            }
        }
    }

    public static JwtInfo check(FullHttpRequest fullHttpRequest, Route route) {

        String jwtString = fullHttpRequest.headers().get("Authentication");
        if (jwtString == null || JwtUtil.validateToken(jwtString)) {
            return new JwtInfo(false);
        }

        User currentUser = null;

        for (UserDetailService roleDetails : roleDetails) {
            if (roleDetails.loadUser(JwtUtil.getIdFromToken(jwtString)) != null) {
                currentUser = roleDetails.loadUser(JwtUtil.getIdFromToken(jwtString));
            }
        }

        if (currentUser == null) {
            return new JwtInfo(false);
        }

        Role role = route.getMethod().getAnnotation(Role.class);
        String[] permitRoles = role.value();

        for (String role1 : currentUser.getRoles()) {
            for (String role2 : permitRoles) {
                if (role1.equals(role2)) {
                    UserDetailSetter(route, currentUser);
                    return new JwtInfo(true);
                }
            }
        }

        return new JwtInfo(false);

    }

    public static void UserDetailSetter(Route route, User user) {
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
