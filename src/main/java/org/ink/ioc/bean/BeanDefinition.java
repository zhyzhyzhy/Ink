package org.ink.ioc.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * info for one class
 * contains beanName, like {@code @Bean(name = "demo"} default is {@code class.getName()}
 *
 * contains beanClass the object of the bean's class
 *
 * contains bean's object

 *
 * @author zhuyichen
 * since 2017.4.9
 */
public class BeanDefinition {

    /**
     * bean's name
     * can set by {@code Bean(name = ""}
     * or default is {@code class.getName()}
     */
    private String name;

    /**
     * the class of the bean
     */
    private Class clazz;

    /**
     * the object of the bean
     */
    private Object object;

    /**
     * singleton
     * default is false
     */
    private boolean singleton = false;


    public BeanDefinition(Class beanClassName, Object object) {
        this(beanClassName.getName(), beanClassName, object);
    }

    public BeanDefinition(String name, Class clazz, Object object) {
        this.name = name;
        this.clazz = clazz;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("clazz", clazz)
                .append("object", object)
                .append("singleton", singleton)
                .toString();
    }
}

