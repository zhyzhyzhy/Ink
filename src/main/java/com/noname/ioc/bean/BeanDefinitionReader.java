package com.noname.ioc.bean;


import com.noname.NoName;
import com.noname.NoNameConfigure;
import com.noname.aop.annotation.Proxy;
import com.noname.db.MybatisConfig;
import com.noname.db.Service;
import com.noname.ioc.annotation.Bean;
import com.noname.ioc.annotation.Component;
import com.noname.ioc.annotation.Inject;
import com.noname.web.annotation.Controller;
import org.apache.ibatis.annotations.Mapper;
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
 *
 * 用于读取bean的信息
 */
public class BeanDefinitionReader {

    private BeanDefinitionRegister register;
    private BeanMetaDataResolver resolver = new BeanMetaDataResolver();
    private List<Class> classList = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(BeanDefinitionReader.class);


    public BeanDefinitionReader(BeanDefinitionRegister register) {
        this.register = register;
    }

    public void configure(Class<?> configureClass) {
        try {
            Object object = configureClass.newInstance();
            this.register.registerBean(configureClass.newInstance());
            NoNameConfigure configure = (NoNameConfigure)object;
            MybatisConfig.configure(object);
            for (String beanPackage : configure.beansPackage()) {
                log.info("package {} is being scan", beanPackage);
                addPackageToScan(beanPackage);
            }
        } catch (Exception e) {
            log.info("class {} did't implements NoNameConfigure.class", configureClass);
        } finally {
            classList.add(configureClass);
            configureInjectField();
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
                            //先从containner中寻找bean
                            Object object = this.register.getBean(Class.forName(field.getGenericType().toString().split(" ")[1]));

                            if (object == null) {
                                object = this.register.getBean(field.getType().getName());
                            }
                            //如果没找到，则new一个
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

                for (File f : file.listFiles()) {

                    //如果还是个包，则继续扫描
                    if (f.isDirectory()) {
                        addPackageToScan(pack + "/" + f.getName());
                    }
                    else {
                        Class<?> class1 = Class.forName(pack + "." + f.getName().replace(".class", ""));
                        if (containBeanAnnotation(class1)) {
                            classList.add(class1);
                            this.register.registerBean(class1.newInstance());
                            log.info("register bean {}", class1);
                        }
                        if (class1.getAnnotation(Mapper.class) != null) {
                            MybatisConfig.addMapper(class1);
                            log.info("add mapper {}", class1);
                            MybatisConfig.buildSessionFactory();
                            this.register.registerBean(class1.getName(), MybatisConfig.getMapper(class1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean containBeanAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(Component.class) != null
                || clazz.getAnnotation(Controller.class) != null
                || clazz.getAnnotation(Service.class) != null
                || clazz.getAnnotation(Proxy.class) != null;
    }

}
