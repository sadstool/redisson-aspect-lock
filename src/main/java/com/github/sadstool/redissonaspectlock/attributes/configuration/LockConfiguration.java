package com.github.sadstool.redissonaspectlock.attributes.configuration;

public class LockConfiguration {

    private long waitTime;
    private long leaseTime;

    public LockConfiguration(long waitTime, long leaseTime) {
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }
}
