package com.github.sadstool.redissonaspectlock.attributes;

import com.github.sadstool.redissonaspectlock.annotation.Lockable;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfiguration;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfigurationProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

public class LockAttributesProvider {

    public static final String LOCK_PATH_PREFIX = "lock.";

    private LockKeysProvider keyProvider;
    private LockConfigurationProvider configurationProvider;

    public LockAttributesProvider(LockKeysProvider keyProvider, LockConfigurationProvider configurationProvider) {
        this.keyProvider = keyProvider;
        this.configurationProvider = configurationProvider;
    }

    public LockAttributes get(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Lockable lockAnnotation = methodSignature.getMethod().getAnnotation(Lockable.class);

        String name = getName(lockAnnotation.value(), methodSignature);
        LockConfiguration lockConfiguration = configurationProvider.getConfiguration(name);
        long waitTime = getWaitTime(lockConfiguration, lockAnnotation);
        long leaseTime = getLeaseTime(lockConfiguration, lockAnnotation);

        List<String> keys = keyProvider.get(methodSignature.getMethod().getParameters(), joinPoint.getArgs());
        String path = getPath(name, keys);

        return new LockAttributes(name, path, waitTime, leaseTime, keys);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }

    private long getWaitTime(LockConfiguration configuration, Lockable annotation) {
        return annotation.waitTime() == Long.MIN_VALUE ?
                configuration.getWaitTime() : annotation.waitTime();
    }

    private long getLeaseTime(LockConfiguration configuration, Lockable annotation) {
        return annotation.leaseTime() == Long.MIN_VALUE ?
                configuration.getLeaseTime() : annotation.leaseTime();
    }

    private String getPath(String name, List<String> keys) {
        StringBuilder stringBuilder = new StringBuilder(LOCK_PATH_PREFIX).append(name);
        for (String key : keys) {
            stringBuilder.append('.').append(key);
        }

        return stringBuilder.toString();
    }
}
