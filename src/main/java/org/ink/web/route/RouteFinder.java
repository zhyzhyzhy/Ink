package org.ink.web.route;


import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.ink.exception.UnauthorizedException;
import org.ink.web.annotation.PathVariable;
import org.ink.web.http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhuyichen 2017-7-12
 */
public class RouteFinder {

    //保存路由参数的Pattern和路由的map
    private static final Map<Pattern, Route> routeMap = new HashMap<>();


    //根据参数进行正则替换
    public static Pattern pathCompiler(String path, Method method) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getAnnotations() == null) {
                continue;
            }
            Annotation annotation = parameter.getAnnotations()[0];
            if (annotation instanceof PathVariable) {
                //如果是字符串
                if (parameter.getType() == String.class) {
                    path = path.replace("{" + parameter.getName() + "}", "[0-9\\d\\D]*");
                }
                //如果是数字
                else if (parameter.getType() == Integer.class
                        || parameter.getType() == Long.class) {
                    System.out.println(parameter.getName());
                    path = path.replace("{" + parameter.getName() + "}", "[0-9]*");
                }

            }
        }
        return Pattern.compile(path);
    }

    public static void addRouter(Pattern pattern, Route route) {
        routeMap.put(pattern, route);
    }


    public static Route findRoute(Request request) throws UnauthorizedException {

        String path = request.uri();
        HttpMethod method = request.method();


        //如果有params，找到问号的位置
        int splitIndex = path.indexOf('?');
        //如果没有问号，则index为请求uri的长度
        if (splitIndex == -1) {
            splitIndex = path.length();
        }
        //如果最后一个字符是'/',去掉
        if (path.charAt(splitIndex-1) == '/') {
            splitIndex--;
        }

        //遍历map
        for (Pattern pattern : routeMap.keySet()) {
            //如果找到uri匹配的
            if (pattern.matcher(path.substring(0, splitIndex)).matches()) {

                Route route = routeMap.get(pattern);

                //如果请求方式一样
                if (route.getHttpMethod().equals(method)) {

                    return route;
                }
            }
        }
        return null;
    }


}

