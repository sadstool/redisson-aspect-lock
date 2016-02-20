package com.github.sadstool.redissonaspectlock.config;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisProperties.getHost() + ":" + redisProperties.getPort());
        singleServerConfig.setDatabase(redisProperties.getDatabase());
        if (redisProperties.getPassword() != null) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }

        return Redisson.create(config);
    }
}
