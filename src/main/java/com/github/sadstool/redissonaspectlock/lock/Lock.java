package com.github.sadstool.redissonaspectlock.lock;

public interface Lock {

    boolean acquire();

    void release();
}
