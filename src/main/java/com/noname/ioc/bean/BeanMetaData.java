package com.noname.ioc.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhy on 4/14/17.
 */
public class BeanMetaData {
    private String beanName;
    private Class<?> beanClass;
    private Annotation[] annotations;
    private Method[] methods;
    private Constructor[] constructors;
    private Field[] fields;

    public BeanMetaData() {

    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public Constructor[] getConstructors() {
        return constructors;
    }

    public void setConstructors(Constructor[] constructors) {
        this.constructors = constructors;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
