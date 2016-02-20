package com.github.sadstool.redissonaspectlock.error;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;

public class LockExceptionFactory {

    public Exception create(LockAttributes attributes) {
        return new LockException("Resource is locked");
    }
}
