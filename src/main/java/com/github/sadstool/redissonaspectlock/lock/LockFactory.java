package com.github.sadstool.redissonaspectlock.lock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockFactory.class);

    private RedissonClient redissonClient;

    public LockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Lock create(LockAttributes attributes) {
        String path = attributes.getPath();
        long waitTime = attributes.getWaitTime();
        long leaseTime = attributes.getLeaseTime();

        LOGGER.trace("Creating lock with name '{}', path '{}', waitTime {} and leaseTime {}", attributes.getName(),
                path, waitTime, leaseTime);
        RLock lock = redissonClient.getLock(path);

        return new RedissonLock(lock, waitTime, leaseTime);
    }
}
