package com.github.sadstool.redissonaspectlock.config.properties.name;

public class LockNameProperties {

    protected String pattern;
    protected Long waitTime;
    protected Long leaseTime;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public Long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(Long leaseTime) {
        this.leaseTime = leaseTime;
    }
}
