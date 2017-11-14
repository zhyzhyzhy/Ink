package org.ink.ioc;


import org.ink.ioc.bean.BeanDefinition;
import org.ink.ioc.bean.BeanDefinitionReader;
import org.ink.ioc.bean.BeanDefinitionRegister;
import org.ink.ioc.bean.BeanFactory;

import java.util.Map;


/**
 * Created by zhy on 4/14/17.
 */
public class IocContext implements BeanDefinitionRegister {

    private BeanFactory beanFactory = new BeanFactory();
    private BeanDefinitionReader beanDefinitionReader;

    public IocContext() {
        beanDefinitionReader = new BeanDefinitionReader(this);
    }

    public IocContext(Class<?> configure) {
        this();
        beanDefinitionReader.configure(configure);
    }

    @Override
    public void registerBean(String name, Object object) {
        beanFactory.registerBean(name, object);
    }

    @Override
    public void registerBean(Object object) {
        beanFactory.registerBean(object);
    }

    @Override
    public <T> T getBean(String name) {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class beanClass) {
        return beanFactory.getBean(beanClass);
    }

    @Override
    public Map<String, BeanDefinition> getDefinitions() {
        return beanFactory.getContainer();
    }

}
