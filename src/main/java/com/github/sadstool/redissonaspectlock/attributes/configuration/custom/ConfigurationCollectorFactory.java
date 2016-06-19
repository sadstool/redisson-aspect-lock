package com.github.sadstool.redissonaspectlock.attributes.configuration.custom;


import com.github.sadstool.redissonaspectlock.attributes.configuration.DefaultConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfiguration;
import com.github.sadstool.redissonaspectlock.config.properties.name.LockNameProperties;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ConfigurationCollectorFactory {

    private LockConfiguration defaultConfiguration;

    public ConfigurationCollectorFactory(DefaultConfigurationProvider defaultConfigurationProvider) {
        this.defaultConfiguration = defaultConfigurationProvider.getDefaultConfiguration();
    }

    public Collector<LockNameProperties, ?, Map<Pattern, LockConfiguration>> createCollector() {
        return Collectors.toMap(this::prepareNamePattern, this::prepareConfiguration);
    }

    private Pattern prepareNamePattern(LockNameProperties lockProperties) {
        return Pattern.compile(lockProperties.getPattern(), Pattern.CASE_INSENSITIVE);
    }

    private LockConfiguration prepareConfiguration(LockNameProperties lockProperties) {
        long waitTime = lockProperties.getWaitTime() != null
                ? lockProperties.getWaitTime() : defaultConfiguration.getWaitTime();
        long leaseTime = lockProperties.getLeaseTime() != null
                ? lockProperties.getLeaseTime() : defaultConfiguration.getLeaseTime();

        return new LockConfiguration(waitTime, leaseTime);
    }
}
