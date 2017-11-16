package org.ink.ioc.bean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhy on 2017/4/9.
 */
public class BeanFactory implements BeanDefinitionRegister {
    private Map<String, BeanDefinition> container = new ConcurrentHashMap<String, BeanDefinition>();

    public BeanFactory() {
    }

    @Override
    public void registerBean(String name, Object object) {
        if (!container.containsKey(name)) {
            BeanDefinition bean = new BeanDefinition(object.getClass(), object);
            container.put(name, bean);
        }
    }

    @Override
    public void registerBean(Object object) {
        registerBean(object.getClass().getName(), object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        if (container.containsKey(name)) {
            return (T) container.get(name).getObject();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class beanClass) {
        for (BeanDefinition class1 : container.values()) {
            if (class1.getClazz() == beanClass) {
                return (T) class1.getObject();
            }
        }
        return null;
    }

    @Override
    public Map<String, BeanDefinition> getDefinitions() {
        return container;
    }

    public Map<String, BeanDefinition> getContainer() {
        return container;
    }

}
