package org.ink.ioc.bean;

import java.util.Map;

/**
 * Created by zhy on 4/14/17.
 */
public interface BeanDefinitionRegister {
    void registerBean(String name, Object object);

    void registerBean(Object object);

    @SuppressWarnings("unchecked")
    <T> T getBean(String name);

    @SuppressWarnings("unchecked")
    <T> T getBean(Class beanClass);

    Map<String, BeanDefinition> getDefinitions();
}
