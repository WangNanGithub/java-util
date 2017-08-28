/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with haitouhui.com.
 */
package com.htouhui.pdl.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 在集群环境下，多台服务器执行同一个任务时，仅当优先获得这个锁的服务器才能唯一执行此任务。
 *
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
@Component
public class OnePassRedisLock {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public OnePassRedisLock(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean get(String key, TimeUnit timeUnit) {
        String timestamp;
        if (timeUnit == TimeUnit.HOURS) {
            timestamp = FastDateFormat.getInstance("yyyyMMddHH").format(new Date());
        }
        else {
            throw new UnsupportedOperationException("TimeUnit Not Support..." + timeUnit);
        }
        String redisKey = key + timestamp;
        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(redisKey);
        boolean isLock = operations.setIfAbsent(redisKey);
        redisTemplate.expire(redisKey, 5, TimeUnit.DAYS);
        return isLock;
    }
}
