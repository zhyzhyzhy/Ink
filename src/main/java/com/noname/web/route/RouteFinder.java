package com.noname.web.route;



import com.noname.web.annotation.GET;
import com.noname.web.annotation.POST;
import com.noname.web.annotation.PathVariable;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class RouteFinder {
    private static Map<Pattern, Route> routeMap = new HashMap<>();

    public static Pattern pathCompiler(String path, Method method) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getAnnotations() == null) {
                continue;
            }
            Annotation annotation = parameter.getAnnotations()[0];
            if (annotation instanceof PathVariable) {
                if (parameter.getType() == String.class) {
                    path = path.replace("{" + parameter.getName()+"}","[0-9\\d\\D]*");
                }
                else if (parameter.getType() == Integer.class
                        || parameter.getType() == Long.class) {

                    path = path.replace("{" + parameter.getName()+"}","[0-9]*");
                }

            }
        }
        return Pattern.compile(path);
    }

    public static void addRouter(Pattern pattern, Route route) {
        routeMap.put(pattern,route);
    }
    public static Route findRoute(String path, HttpMethod method) {
        for (Pattern pattern : routeMap.keySet()) {
            if (pattern.matcher(path).matches()) {
                if (routeMap.get(pattern).getHttpMethod().equals(method)) {
                    routeParamtersSetter(path, routeMap.get(pattern));
                    return routeMap.get(pattern);
                }
            }
        }
        return null;
    }
    public static void routeParamtersSetter(String path, Route route) {
        Method method = route.getMethod();
        Annotation annotation = method.getAnnotations()[0];
        String uri = "";
        if (annotation instanceof GET) {
            uri = ((GET)annotation).value();
        }
        if (annotation instanceof POST) {
            uri = ((POST)annotation).value();
        }
        String[] requestPaths = path.split("/");
        String[] originPath = uri.split("/");

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < requestPaths.length; i++) {
            if (!requestPaths[i].equals(originPath[i])) {
                for (int j = 0; j < parameters.length; j++) {
                    if (parameters[j].getName().equals(originPath[i].substring(1, originPath[i].length() - 1))) {
                        route.getParamters()[j] = requestPaths[i];
                    }
                }
            }
        }
    }

}
