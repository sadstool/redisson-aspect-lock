package com.github.sadstool.redissonaspectlock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Lockable {

    String value() default "";
    long waitTime() default Long.MIN_VALUE;
    long leaseTime() default Long.MIN_VALUE;
}
