package com.github.sadstool.redissonaspectlock.lock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LockCollection {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockCollection.class);

    private Map<Lock, LockAttributes> locks;
    private Function<LockAttributes, Exception> errorFactory;

    public LockCollection(Map<Lock, LockAttributes> locks, Function<LockAttributes, Exception> errorFactory) {
        this.locks = locks;
        this.errorFactory = errorFactory;
    }

    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        acquire();
        try {
            return joinPoint.proceed();
        } finally {
            release();
        }
    }

    private void acquire() throws Exception {
        List<Lock> acquiredLocks = new ArrayList<>();
        Optional<Lock> failed = Optional.empty();

        for (Lock lock : locks.keySet()) {
            if (!acquire(lock)) {
                failed = Optional.of(lock);
                break;
            } else {
                acquiredLocks.add(lock);
            }
        }

        if (failed.isPresent()) {
            LockAttributes failedLockAttributes = locks.get(failed.get());
            LOGGER.debug("Locking failed: {}", failedLockAttributes.getPath());
            acquiredLocks.forEach(this::release);

            throw errorFactory.apply(failedLockAttributes);
        } else {
            LOGGER.trace("Locked");
        }
    }

    private boolean acquire(Lock lock) {
        LOGGER.debug("Locking: {}", locks.get(lock).getPath());

        return lock.acquire();
    }

    private void release() {
        locks.keySet().forEach(this::release);
    }

    private void release(Lock lock) {
        LOGGER.debug("Unlocking: {}", locks.get(lock).getPath());

        lock.release();
    }

    public static class Builder {
        private List<LockAttributes> attributes;
        private Function<LockAttributes, Lock> lockProvider;
        private Function<LockAttributes, Exception> errorFactory;

        public Builder setAttributes(List<LockAttributes> attributes) {
            this.attributes = attributes;

            return this;
        }

        public Builder setLockProvider(Function<LockAttributes, Lock> lockProvider) {
            this.lockProvider = lockProvider;

            return this;
        }

        public Builder setErrorFactory(Function<LockAttributes, Exception> errorFactory) {
            this.errorFactory = errorFactory;

            return this;
        }

        public LockCollection build() {
            Map<Lock, LockAttributes> map = attributes.stream()
                    .collect(Collectors.toMap(attributes -> lockProvider.apply(attributes), Function.identity()));

            return new LockCollection(map, errorFactory);
        }
    }
}
