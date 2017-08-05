package com.noname.filter;

import com.noname.ioc.bean.BeanDefinition;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FilterUtil {

    private static final Logger log = LoggerFactory.getLogger(FilterUtil.class);

    private static final Map<Pattern, BeforeFilter> beforeFilterMap = new HashMap<>();
//    public static final Map<Pattern, AfterFilter> afterFilterMap = new HashMap<>();

    public static void registerFilter(Map<String, BeanDefinition> beanDefinitionMap) {
        log.info("start register filter...");
        for (String key : beanDefinitionMap.keySet()) {

            BeanDefinition beanDefinition = beanDefinitionMap.get(key);

            if (beanDefinition.getBeanClassName().getAnnotation(Filter.class) == null) {
                continue;
            }

            Object object = beanDefinition.getObject();

            Filter filterAnnotation = (Filter) object.getClass().getAnnotation(Filter.class);
            Pattern pattern = Pattern.compile(filterAnnotation.value());

            Class<?> interfaces[] = object.getClass().getInterfaces();
            for (Class<?> intr : interfaces) {
                if (intr.equals(BeforeFilter.class)) {
                    beforeFilterMap.put(pattern, (BeforeFilter) object);
                    log.info("register filter {}", filterAnnotation.value());
                }
            }
        }
    }

    public static boolean doFilter(Request request, Response response) {
        for (Pattern pattern : beforeFilterMap.keySet()) {
            if (pattern.matcher(request.getUri()).matches()) {
                if(!beforeFilterMap.get(pattern).doFilter(request, response)) {
                    return false;
                }
            }
        }
        return true;
    }
}
