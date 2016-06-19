package com.github.sadstool.redissonaspectlock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Lockable {

    /**
     * Lock name
     * @return lock name
     */
    String value() default "";

    /**
     * The maximum time (millis) to acquire the lock
     * @return maximum wait time
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     * The maximum time (millis) to hold the lock after granting it,
     * before automatically releasing it if it hasn't already been released
     * @return maximum lease time
     */
    long leaseTime() default Long.MIN_VALUE;
}
