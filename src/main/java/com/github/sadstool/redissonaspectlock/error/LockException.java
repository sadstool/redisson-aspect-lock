package com.github.sadstool.redissonaspectlock.error;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;

public class LockException extends RuntimeException {

    private LockAttributes attributes;

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, LockAttributes attributes) {
        this(message);
        this.attributes = attributes;
    }

    public LockAttributes getAttributes() {
        return attributes;
    }
}
