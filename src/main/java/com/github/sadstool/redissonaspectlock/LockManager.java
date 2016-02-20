package com.github.sadstool.redissonaspectlock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import com.github.sadstool.redissonaspectlock.attributes.LockAttributesProvider;
import com.github.sadstool.redissonaspectlock.error.LockExceptionFactory;
import com.github.sadstool.redissonaspectlock.lock.Lock;
import com.github.sadstool.redissonaspectlock.lock.LockFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LockManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockManager.class);

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
        LockAttributes attributes = attributesProvider.get(joinPoint);
        Lock lock = lockFactory.create(attributes);

        LOGGER.debug("Locking: {}", attributes.getPath());
        if (!lock.acquire()) {
            LOGGER.debug("Locking failed");
            throw exceptionFactory.create(attributes);
        }

        LOGGER.trace("Locked");
        try {
            return joinPoint.proceed();
        } finally {
            LOGGER.debug("Unlocking: {}", attributes.getPath());
            lock.release();
        }
    }
}
