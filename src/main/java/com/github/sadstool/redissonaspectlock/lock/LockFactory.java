package com.github.sadstool.redissonaspectlock.lock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;

public class LockFactory {

    private RedissonClient redissonClient;

    public LockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Lock create(LockAttributes attributes) {
        RLock lock = redissonClient.getLock(attributes.getPath());

        return new RedissonLock(lock, attributes.getWaitTime(), attributes.getLeaseTime());
    }
}
