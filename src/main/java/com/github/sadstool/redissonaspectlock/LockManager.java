package com.github.sadstool.redissonaspectlock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributesProvider;
import com.github.sadstool.redissonaspectlock.error.LockExceptionFactory;
import com.github.sadstool.redissonaspectlock.lock.LockCollection;
import com.github.sadstool.redissonaspectlock.lock.LockFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LockManager {
    private LockAttributesProvider attributesProvider;
    private LockFactory lockFactory;
    private LockExceptionFactory exceptionFactory;

    @Autowired
    public LockManager(LockAttributesProvider attributesProvider, LockFactory lockFactory, LockExceptionFactory exceptionFactory) {
        this.attributesProvider = attributesProvider;
        this.lockFactory = lockFactory;
        this.exceptionFactory = exceptionFactory;
    }

    @Around(value = "@annotation(com.github.sadstool.redissonaspectlock.annotation.Lockable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LockCollection lockCollection = new LockCollection.Builder()
                .setAttributes(attributesProvider.get(joinPoint))
                .setLockProvider(lockFactory::create)
                .setErrorFactory(attributes -> exceptionFactory.create(attributes))
                .build();

        return lockCollection.proceed(joinPoint);
    }
}
