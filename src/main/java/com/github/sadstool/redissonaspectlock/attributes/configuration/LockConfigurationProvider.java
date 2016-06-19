package com.github.sadstool.redissonaspectlock.attributes.configuration;


import com.github.sadstool.redissonaspectlock.attributes.configuration.custom.CustomConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockConfigurationProvider.class);

    private CustomConfigurationProvider customConfigurationProvider;
    private Map<String, LockConfiguration> staticConfiguration = new ConcurrentHashMap<>();

    public LockConfigurationProvider(CustomConfigurationProvider customConfigurationProvider) {
        this.customConfigurationProvider = customConfigurationProvider;
    }

    public LockConfiguration getConfiguration(String name) {
        if (!staticConfiguration.containsKey(name)) {
            LOGGER.trace("No static configuration found for name '{}'", name);
            LockConfiguration configuration = customConfigurationProvider.getConfiguration(name);
            staticConfiguration.put(name, configuration);
        } else {
            LOGGER.trace("Static configuration found for name '{}'", name);
        }

        return staticConfiguration.get(name);
    }
}
