package org.ink.web.route;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.ink.exception.UnauthorizedException;
import org.ink.security.JwtInfo;
import org.ink.security.SecurityManager;
import org.ink.web.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public final class RouteSetter {

    private static final Logger logger = LoggerFactory.getLogger(RouteSetter.class);

    public static void routeSetter(Route route, FullHttpRequest fullHttpRequest) throws UnauthorizedException {
        String path = fullHttpRequest.uri();
        HttpMethod method = fullHttpRequest.method();


        if (route.isSecurity()) {
            JwtInfo jwtInfo = SecurityManager.check(fullHttpRequest, route);
            if (!jwtInfo.isOk()) {
                throw new UnauthorizedException();
            }
        }

        //设置@PathVariable
        routePathVariableSetter(path, route);

        if (route.getHttpMethod().equals(HttpMethod.GET)) {
            //设置GET @RequestParam
            GETParamsSetter(path, route);
        } else if (route.getHttpMethod().equals(HttpMethod.POST)) {
            //设置POST @RequestParam
            POSTParamsSetter(fullHttpRequest, route);
        }

        //设置@RequestJson
        if ("application/json".equals(fullHttpRequest.headers().get("content-Type"))) {
            routeRequestJsonSetter(fullHttpRequest.content().copy().toString(CharsetUtil.UTF_8), route);
        }
    }

    //处理@RequestJson参数
    public static void routeRequestJsonSetter(String json, Route route) {
        Method method = route.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Annotation[] annotations = parameters[i].getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestJson) {
                    //得到参数的类
                    Class<?> class1 = parameters[i].getType();
                    Object object = JSON.parseObject(json, class1);
                    route.getParamters()[i] = object;
                }
            }
        }
    }


    //处理@PathVariable参数
    public static void routePathVariableSetter(String path, Route route) {
        Method method = route.getMethod();
        Annotation annotation = method.getAnnotations()[0];
        String uri = "";


        if (annotation instanceof GET) {
            uri = ((GET) annotation).value();
        } else if (annotation instanceof POST) {
            uri = ((POST) annotation).value();
        } else if (annotation instanceof PUT) {
            uri = ((PUT) annotation).value();
        } else if (annotation instanceof DELETE) {
            uri = ((DELETE) annotation).value();
        }

        String[] requestPaths = path.split("/");
        String[] originPath = uri.split("/");

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < requestPaths.length && i < originPath.length; i++) {
            if (!requestPaths[i].equals(originPath[i])) {
                for (int j = 0; j < parameters.length; j++) {
                    if (parameters[j].getName().equals(originPath[i].substring(1, originPath[i].length() - 1))) {
                        //根据@PathVariable的类型进行转换
                        if (parameters[j].getType().equals(Integer.class)) {
                            route.getParamters()[j] = Integer.valueOf(requestPaths[i]);
                        } else if (parameters[j].getType().equals(Long.class)) {
                            route.getParamters()[j] = Long.valueOf(requestPaths[i]);
                        } else if (parameters[j].getType().equals(String.class)) {
                            route.getParamters()[j] = requestPaths[i];
                        }
                    }
                }
            }
        }
    }

    //处理@RequestParam参数
    public static void GETParamsSetter(String path, Route route) {

        QueryStringDecoder decoder = new QueryStringDecoder(path);
        Map<String, List<String>> map = decoder.parameters();
        Parameter[] parameters = route.getMethod().getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Annotation[] annotations = parameters[i].getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestParam) {
                    List<String> list = map.get(parameters[i].getName());
                    if (list == null || list.size() == 0) {
                        route.getParamters()[i] = null;
                    } else {
                        route.getParamters()[i] = list.get(0);
                    }
                }
            }
        }

    }

    //处理@POSTParam参数
    public static void POSTParamsSetter(FullHttpRequest request, Route route) {
        Parameter[] parameters = route.getMethod().getParameters();


        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
        List<InterfaceHttpData> interfaceHttpData = decoder.getBodyHttpDatas();

        interfaceHttpData.stream()
                .forEach(interfaceHttpData1 -> {
                    //if is Attribute
                    if (interfaceHttpData1.getHttpDataType().equals(InterfaceHttpData.HttpDataType.Attribute)) {

                        Attribute attribute = (Attribute) interfaceHttpData1;

                        for (int i = 0; i < parameters.length; i++) {
                            Annotation[] annotations = parameters[i].getAnnotations();
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof RequestParam) {
                                    String parameterName = parameters[i].getName();
                                    if (parameterName.equals(interfaceHttpData1.getName())) {
                                        try {
                                            route.getParamters()[i] = attribute.getValue();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

}
