package com.noname.security;

import com.noname.NoNameConfigure;
import com.noname.db.Service;
import com.noname.ioc.bean.BeanDefinition;
import com.noname.ioc.context.IocContext;
import com.noname.security.annotation.Role;
import com.noname.security.annotation.UserDetail;
import com.noname.web.route.Route;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
            if (beanDefinition.getObject().getClass().getAnnotation(Service.class) != null) {
                roleDetails.add((UserDetailService) beanDefinition.getObject());
            }
        }

        Object ob = iocContext.getBean(configureclass);
        if (ob instanceof NoNameConfigure) {
            NoNameConfigure noNameConfigure = (NoNameConfigure) ob;
            iocContext.registerBean(new AuthenticationRoutes());
            SecurityConfig.KEY = noNameConfigure.SecurityKey();
            System.out.printf(noNameConfigure.SecurityKey());
            SecurityConfig.anthenticationOpen = noNameConfigure.anthenticationOpen();
        }


    }

    public static User login(String userName, String password) {

        User currentUser = null;
        for (UserDetailService roleDetails : roleDetails) {
            currentUser = roleDetails.loadUser(userName);
            if (currentUser != null && currentUser.getPassword().equals(password)) {
                return currentUser;
            }
        }
        return currentUser;
    }

    public static JwtInfo check(FullHttpRequest fullHttpRequest, Route route) {

        String jwtString = fullHttpRequest.headers().get("Authorization");
        if (jwtString == null || !JwtUtil.validateToken(jwtString)) {
            return new JwtInfo(false);
        }

        User currentUser = JwtUtil.getUserFromToken(jwtString);

        Role role = route.getMethod().getAnnotation(Role.class);
        String[] permitRoles = role.value();

        System.out.println(currentUser.getUserName());
        for (String role1 : currentUser.getRoles()) {
            System.out.println(role1);
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
