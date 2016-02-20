package com.github.sadstool.redissonaspectlock.error;

public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }
}
