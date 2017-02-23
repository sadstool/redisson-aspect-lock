package com.github.sadstool.redissonaspectlock.attributes;

import com.github.sadstool.redissonaspectlock.annotation.Lockable;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfiguration;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.key.LockKeyProvider;
import com.github.sadstool.redissonaspectlock.error.LockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LockAttributesProvider {

    public static final String LOCK_PATH_PREFIX = "lock";
    public static final String LOCK_PATH_SEPARATOR = ".";

    private LockKeyProvider keyProvider;
    private LockConfigurationProvider configurationProvider;

    public LockAttributesProvider(LockKeyProvider keyProvider, LockConfigurationProvider configurationProvider) {
        this.keyProvider = keyProvider;
        this.configurationProvider = configurationProvider;
    }

    public List<LockAttributes> get(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = getMethod(joinPoint, signature);

        Lockable annotation = method.getAnnotation(Lockable.class);
        Object[] arguments = joinPoint.getArgs();

        String name = getName(annotation.value(), signature);
        LockConfiguration configuration = configurationProvider.getConfiguration(name);
        long waitTime = getWaitTime(configuration, annotation);
        long leaseTime = getLeaseTime(configuration, annotation);
        List<List<String>> keyCollections = getKeyCollections(method, annotation, arguments);

        return keyCollections.stream()
                .map(keys -> new LockAttributes(name, getPath(name, keys), waitTime, leaseTime, keys))
                .collect(Collectors.toList());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint, MethodSignature signature) {
        Method method = signature.getMethod();

        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                        method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new LockException("Unable to get target method");
            }
        }

        return method;
    }

    private List<List<String>> getKeyCollections(Method method, Lockable annotation, Object[] arguments) {
        if (annotation.key().length == 0) {
            return Collections.singletonList(keyProvider.get(null, method, arguments));
        } else {
            return Stream.of(annotation.key())
                    .map(keyDefinition -> keyProvider.get(keyDefinition, method, arguments))
                    .collect(Collectors.toList());
        }
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
        return Stream.concat(Stream.of(LOCK_PATH_PREFIX, name), keys.stream())
                .collect(Collectors.joining(LOCK_PATH_SEPARATOR));
    }
}
