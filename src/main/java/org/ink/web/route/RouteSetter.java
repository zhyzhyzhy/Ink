package org.ink.web.route;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.ink.security.exception.UnauthorizedException;
import org.ink.security.CheckResult;
import org.ink.security.SecurityManager;
import org.ink.web.WebContext;
import org.ink.web.annotation.*;
import org.ink.web.view.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ink.web.route.RouteSetter.modelSetter;

public final class RouteSetter {

    private static final Logger logger = LoggerFactory.getLogger(RouteSetter.class);

    public static void routeSetter(Route route, FullHttpRequest fullHttpRequest) throws Exception {
        String path = fullHttpRequest.uri();
        HttpMethod method = fullHttpRequest.method();


        if (route.security()) {
            CheckResult checkResult = SecurityManager.check(route);
            if (!checkResult.isOk()) {
                throw checkResult.exception();
            }
        }

        //设置@PathVariable
        routePathVariableSetter(path, route);

        if (route.httpMethod().equals(HttpMethod.GET)) {
            //设置GET @RequestParam
            GETParamsSetter(path, route);
        } else if (route.httpMethod().equals(HttpMethod.POST)) {
            //设置POST @RequestParam
            POSTParamsSetter(fullHttpRequest, route);
        }

        //设置@RequestJson
        if ("application/json".equals(fullHttpRequest.headers().get("content-Type"))) {
            routeRequestJsonSetter(fullHttpRequest.content().copy().toString(CharsetUtil.UTF_8), route);
        }

        //设置@FILE
        if (fullHttpRequest.headers().get("content-Type") != null && fullHttpRequest.headers().get("content-Type").startsWith("multipart/form-data")) {
            fileSetter(fullHttpRequest, route);
        }

        //设置model
        modelSetter(fullHttpRequest, route);

    }

    //处理Model参数
    public static void modelSetter(FullHttpRequest fullHttpRequest, Route route) {
        Method method = route.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //得到参数的类
            Class<?> class1 = parameters[i].getType();
            if (class1.equals(Model.class)) {
                Model model = new Model();
                WebContext.currentResponse().setModel(model);
                route.getParamters()[i] = model;
            }
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

    public static void fileSetter(FullHttpRequest request, Route route) {
        try {
            HttpPostMultipartRequestDecoder decoder = new HttpPostMultipartRequestDecoder(request);
            Map<String, File> map = new HashMap<>();

            for (InterfaceHttpData httpData : decoder.getBodyHttpDatas()) {
                if ("FileUpload".equals(httpData.getHttpDataType().name())) {
                    FileUpload fileUpload = (FileUpload) httpData;
                    map.put(httpData.getName(), fileUpload.getFile());
                }
            }
            Method method = route.getMethod();
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Annotation[] annotations = parameters[i].getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof FILE) {
                        //得到参数的类
                        if (map.containsKey(parameters[i].getName())) {
                            //根据名字set
                            route.getParamters()[i] = map.get(parameters[i].getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
