package com.github.sadstool.redissonaspectlock.attributes;

import java.util.List;

public class LockAttributes {

    private String name;
    private String path;
    private long waitTime;
    private long leaseTime;
    private List<String> keys;

    public LockAttributes(String name, String path, long waitTime, long leaseTime, List<String> keys) {
        this.name = name;
        this.path = path;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public List<String> getKeys() {
        return keys;
    }
}
