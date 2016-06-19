package com.github.sadstool.redissonaspectlock.attributes.configuration;

import com.github.sadstool.redissonaspectlock.config.properties.LockProperties;

public class DefaultConfigurationProvider {

    public static final long DEFAULT_WAIT_TIME = 0;
    public static final long DEFAULT_LEASE_TIME = 10000;

    private LockConfiguration defaultConfiguration;

    public DefaultConfigurationProvider(LockProperties properties) {
        long defaultWaitTime = (properties.getWaitTime() != null) ? properties.getWaitTime() : DEFAULT_WAIT_TIME;
        long defaultLeaseTime = (properties.getLeaseTime() != null) ? properties.getLeaseTime() : DEFAULT_LEASE_TIME;

        defaultConfiguration = new LockConfiguration(defaultWaitTime, defaultLeaseTime);
    }

    public LockConfiguration getDefaultConfiguration() {
        return defaultConfiguration;
    }
}
