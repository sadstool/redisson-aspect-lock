package com.github.sadstool.redissonaspectlock.config.properties;


import com.github.sadstool.redissonaspectlock.config.properties.name.LockNameProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sadstool.lock")
public class LockProperties {

    private Long waitTime;
    private Long leaseTime;

    private List<LockNameProperties> names;

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

    public List<LockNameProperties> getNames() {
        return names;
    }

    public void setNames(List<LockNameProperties> names) {
        this.names = names;
    }
}
