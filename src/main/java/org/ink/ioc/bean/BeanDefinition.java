package org.ink.ioc.bean;

import java.util.Collections;

/**
 * Created by zhy on 2017/4/9.
 *
 * 存取一个bean的信息
 */
public class BeanDefinition {

    //bean名字
    private String beanName;

    //bean的实际class
    private Class beanClassName;

    //bean的实例
    private Object object;

    //是否是单例的
    private boolean isSingleton = false;

    public BeanDefinition() {

    }

    public BeanDefinition(Class beanClassName, Object object) {
        this(beanClassName.getName(), beanClassName, object);
    }

    public BeanDefinition(String beanName, Class beanClassName, Object object) {
        this.beanName = beanName;
        this.beanClassName = beanClassName;
        this.object = object;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(Class beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanClassName=" + beanClassName +
                ", object=" + object +
                '}';
    }
}
