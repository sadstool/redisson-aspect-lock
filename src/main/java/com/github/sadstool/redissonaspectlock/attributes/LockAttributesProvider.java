package com.github.sadstool.redissonaspectlock.attributes;

import com.github.sadstool.redissonaspectlock.annotation.Lockable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

public class LockAttributesProvider {

    public static final String LOCK_PATH_PREFIX = "lock.";
    public static final long DEFAULT_LEASE_TIME = 10000;
    public static final long DEFAULT_WAIT_TIME = 0;

    private LockKeysProvider keyProvider;

    public LockAttributesProvider(LockKeysProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public LockAttributes get(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Lockable lockAnnotation = methodSignature.getMethod().getAnnotation(Lockable.class);

        List<String> keys = keyProvider.get(methodSignature.getMethod().getParameters(), joinPoint.getArgs());
        String name = getName(lockAnnotation.value(), methodSignature);
        String path = getPath(name, keys);
        long waitTime = getWaitTime(lockAnnotation);
        long leaseTime = getLeaseTime(lockAnnotation);

        return new LockAttributes(name, path, waitTime, leaseTime, keys);
    }

    private String getName(String annotationName, MethodSignature signature) {
        return annotationName.isEmpty() ?
                String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName()) : annotationName;
    }

    private String getPath(String name, List<String> keys) {
        StringBuilder stringBuilder = new StringBuilder(LOCK_PATH_PREFIX).append(name);
        for (String key : keys) {
            stringBuilder.append(".").append(key);
        }
        return stringBuilder.toString();
    }

    private long getWaitTime(Lockable lockAnnotation) {
        return lockAnnotation.waitTime() == Long.MIN_VALUE ?
                DEFAULT_WAIT_TIME : lockAnnotation.waitTime();
    }

    private long getLeaseTime(Lockable lockAnnotation) {
        return lockAnnotation.leaseTime() == Long.MIN_VALUE ?
                DEFAULT_LEASE_TIME : lockAnnotation.leaseTime();
    }
}
