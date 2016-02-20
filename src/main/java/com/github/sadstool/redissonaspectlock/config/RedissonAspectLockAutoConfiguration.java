package com.github.sadstool.redissonaspectlock.config;

import com.github.sadstool.redissonaspectlock.LockManager;
import com.github.sadstool.redissonaspectlock.attributes.LockAttributesProvider;
import com.github.sadstool.redissonaspectlock.attributes.LockKeysProvider;
import com.github.sadstool.redissonaspectlock.error.LockExceptionFactory;
import com.github.sadstool.redissonaspectlock.lock.LockFactory;
import org.redisson.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedissonAutoConfiguration.class, LockManager.class})
public class RedissonAspectLockAutoConfiguration {

    @Bean
    public LockKeysProvider lockKeysProvider() {
        return new LockKeysProvider();
    }

    @Bean
    public LockAttributesProvider lockAttributesProvider(LockKeysProvider lockKeysProvider) {
        return new LockAttributesProvider(lockKeysProvider);
    }

    @Bean
    public LockExceptionFactory lockExceptionFactory() {
        return new LockExceptionFactory();
    }

    @Bean LockFactory lockFactory(RedissonClient redissonClient) {
        return new LockFactory(redissonClient);
    }
}
