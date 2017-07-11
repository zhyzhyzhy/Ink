package com.noname.ioc.bean;


import com.noname.ioc.annotation.Bean;
import com.noname.ioc.annotation.ComponentScan;
import com.noname.ioc.annotation.Inject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zhy on 4/14/17.
 */
public class BeanDefinitionReader {

    private BeanDefinitionRegister register;
    private BeanMetaDataResolver resolver = new BeanMetaDataResolver();
    private List<Class> classList = new ArrayList<Class>();


    public BeanDefinitionReader(BeanDefinitionRegister register) {
        this.register = register;
    }

    public void configure(Class<?> aclass) {
        BeanMetaData metaData = getMetaData(aclass);
        for (Annotation annotation : metaData.getAnnotations()) {
            if (annotation instanceof ComponentScan) {
                ComponentScan annotation1 = (ComponentScan)annotation;
                addPackageToScan(annotation1.value()[0]);
            }
        }
        classList.add(aclass);
        configureInjectField();
    }
    public void configureInjectField() {
        for (Class class1 : classList) {
            BeanMetaData metaData = getMetaData(class1);
            for (Field field : metaData.getFields()) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for ( Annotation annotation : annotations) {
                    if (annotation instanceof Inject) {
                        try {
                            field.setAccessible(true);
                            Object object = this.register.getBean(Class.forName(field.getGenericType().toString().split(" ")[1]));
                            if (object == null) {
                                object = Class.forName(field.getGenericType().toString().split(" ")[1]).newInstance();
                            }
                            field.set(this.register.getBean(class1), object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

    }
    private BeanMetaData getMetaData(Class<?> beanClass) {
        return resolver.getBeanMetaData(beanClass);
    }
    private void addPackageToScan(String pack) {
        try {
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader()
                    .getResources(pack.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getPath());
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.isDirectory()) {
                        addPackageToScan(pack + "/" + f.getName());
                    } else {
                        if (Class.forName(pack + "." + f.getName().replace(".class", "")).getAnnotation(Bean.class) != null) {
                            classList.add(Class.forName(pack + "." + f.getName().replace(".class", "")));
                            this.register.registerBean(Class.forName(pack + "." + f.getName().replace(".class", "")).newInstance());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
