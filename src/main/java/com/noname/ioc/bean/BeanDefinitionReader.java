package com.noname.ioc.bean;


import com.noname.NoNameConfigure;
import com.noname.ioc.annotation.Bean;
import com.noname.ioc.annotation.Component;
import com.noname.ioc.annotation.Inject;
import com.noname.web.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger log = LoggerFactory.getLogger(BeanDefinitionReader.class);


    public BeanDefinitionReader(BeanDefinitionRegister register) {
        this.register = register;
    }

    public void configure(Class<?> aclass) {
        try {
            NoNameConfigure configure = (NoNameConfigure) aclass.newInstance();
            this.register.registerBean(configure);
            for (String beanPackage : configure.beansPackage()) {
                log.info("package {} is being scan", beanPackage);
                addPackageToScan(beanPackage);
            }
            classList.add(aclass);
            configureInjectField();
        } catch (Exception e) {
            log.error("class {} is not implements NoNameConfigure.class", aclass);
        }
    }

    public void configureInjectField() {
        for (Class class1 : classList) {
            log.info("{} is being configure", class1);
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
                        Class<?> class1 = Class.forName(pack + "." + f.getName().replace(".class", ""));
                        if (class1.getAnnotation(Component.class) != null
                                || class1.getAnnotation(Controller.class) != null) {
                            classList.add(class1);
                            this.register.registerBean(class1.newInstance());
                            log.info("register bean {}", class1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
