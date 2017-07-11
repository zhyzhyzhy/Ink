package com.noname.ioc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhy on 4/14/17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    String[] value() default {};
}
