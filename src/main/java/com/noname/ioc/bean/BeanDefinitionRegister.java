package com.noname.ioc.bean;

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
}
