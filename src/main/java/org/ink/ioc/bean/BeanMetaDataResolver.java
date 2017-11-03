package org.ink.ioc.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhy on 4/14/17.
 */
public class BeanMetaDataResolver {
    public BeanMetaData getBeanMetaData(Class<?> beanClass) {
        BeanMetaData beanMetaData = new BeanMetaData();
        beanMetaData.setBeanClass(beanClass);
        beanMetaData.setAnnotations(getAnnotations(beanClass));
        beanMetaData.setFields(getFields(beanClass));
        beanMetaData.setMethods(getMethods(beanClass));
        return beanMetaData;
    }
    public Annotation[] getAnnotations(Class<?> beanClass) {
        try {
           return Class.forName(beanClass.getName()).getAnnotations();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Field[] getFields(Class<?> beanClass) {
        try {
            return Class.forName(beanClass.getName()).getDeclaredFields();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Method[] getMethods(Class<?> beanClass) {
        try {
            return Class.forName(beanClass.getName()).getDeclaredMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
