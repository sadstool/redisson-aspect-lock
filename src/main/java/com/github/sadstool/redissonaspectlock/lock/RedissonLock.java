package com.github.sadstool.redissonaspectlock.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public class RedissonLock implements Lock {

    private RLock lock;
    private long waitTime;
    private long leaseTime;

    public RedissonLock(RLock lock, long waitTime, long leaseTime) {
        this.lock = lock;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public boolean acquire() {
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void release() {
        lock.unlock();
    }
}
