package com.github.sadstool.redissonaspectlock.config;

import com.github.sadstool.redissonaspectlock.LockManager;
import com.github.sadstool.redissonaspectlock.attributes.LockAttributesProvider;
import com.github.sadstool.redissonaspectlock.attributes.configuration.DefaultConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.configuration.custom.ConfigurationCollectorFactory;
import com.github.sadstool.redissonaspectlock.attributes.configuration.custom.CustomConfigurationProvider;
import com.github.sadstool.redissonaspectlock.attributes.key.LockKeyComponentsProvider;
import com.github.sadstool.redissonaspectlock.attributes.key.LockKeyProvider;
import com.github.sadstool.redissonaspectlock.attributes.key.SpelLockKeyProvider;
import com.github.sadstool.redissonaspectlock.config.properties.LockProperties;
import com.github.sadstool.redissonaspectlock.error.LockExceptionFactory;
import com.github.sadstool.redissonaspectlock.lock.LockFactory;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedissonAutoConfiguration.class, LockProperties.class, LockManager.class})
public class RedissonAspectLockAutoConfiguration {

    @Bean
    public SpelLockKeyProvider spelLockKeyProvider() {
        return new SpelLockKeyProvider();
    }

    @Bean
    public LockKeyComponentsProvider lockKeyComponentsProvider() {
        return new LockKeyComponentsProvider();
    }

    @Bean
    public LockKeyProvider lockKeyGenerator(SpelLockKeyProvider spelLockKeyProvider, LockKeyComponentsProvider lockKeyComponentsProvider) {
        return new LockKeyProvider(spelLockKeyProvider, lockKeyComponentsProvider);
    }

    @Bean
    public DefaultConfigurationProvider defaultConfigurationProvider(LockProperties lockProperties) {
        return new DefaultConfigurationProvider(lockProperties);
    }

    @Bean
    public ConfigurationCollectorFactory configurationCollectorFactory(
            DefaultConfigurationProvider defaultConfigurationProvider) {
        return new ConfigurationCollectorFactory(defaultConfigurationProvider);
    }

    @Bean
    public CustomConfigurationProvider customConfigurationProvider(
            ConfigurationCollectorFactory configurationCollectorFactory,
            DefaultConfigurationProvider defaultConfigurationProvider,
            LockProperties lockProperties) {
        return new CustomConfigurationProvider(configurationCollectorFactory, defaultConfigurationProvider,
                lockProperties);
    }

    @Bean
    public LockConfigurationProvider lockConfigurationProvider(
            CustomConfigurationProvider customConfigurationProvider) {
        return new LockConfigurationProvider(customConfigurationProvider);
    }

    @Bean
    public LockAttributesProvider lockAttributesProvider(LockKeyProvider lockKeyProvider,
                                                         LockConfigurationProvider lockConfigurationProvider) {
        return new LockAttributesProvider(lockKeyProvider, lockConfigurationProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public LockExceptionFactory lockExceptionFactory() {
        return new LockExceptionFactory();
    }

    @Bean
    public LockFactory lockFactory(RedissonClient redissonClient) {
        return new LockFactory(redissonClient);
    }
}
