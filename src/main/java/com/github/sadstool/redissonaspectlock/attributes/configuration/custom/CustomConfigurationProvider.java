package com.github.sadstool.redissonaspectlock.attributes.configuration.custom;

import com.github.sadstool.redissonaspectlock.attributes.configuration.DefaultConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfiguration;
import com.github.sadstool.redissonaspectlock.config.properties.LockProperties;
import com.github.sadstool.redissonaspectlock.config.properties.name.LockNameProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class CustomConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomConfigurationProvider.class);

    private ConfigurationCollectorFactory collectorProvider;
    private LockConfiguration defaultConfiguration;
    private Map<Pattern, LockConfiguration> customConfiguration = new HashMap<>();

    public CustomConfigurationProvider(ConfigurationCollectorFactory collectorProvider,
                                       DefaultConfigurationProvider defaultConfigurationProvider,
                                       LockProperties properties) {
        this.collectorProvider = collectorProvider;
        this.defaultConfiguration = defaultConfigurationProvider.getDefaultConfiguration();

        enumerate(properties.getNames());
    }

    private void enumerate(List<LockNameProperties> customProperties) {
        if (customProperties != null) {
            Collector<LockNameProperties, ?, Map<Pattern, LockConfiguration>> collector =
                    collectorProvider.createCollector();

            customConfiguration = customProperties.stream().collect(collector);
        }
    }

    public LockConfiguration getConfiguration(String name) {
        for (Map.Entry<Pattern, LockConfiguration> entry : customConfiguration.entrySet()) {
            Pattern pattern = entry.getKey();
            if (pattern.matcher(name).matches()) {
                LOGGER.trace("Found custom configuration with pattern '{}'", pattern);
                return entry.getValue();
            }
        }

        LOGGER.trace("No custom configuration found, using defaults");
        return defaultConfiguration;
    }
}
