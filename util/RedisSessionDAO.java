/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.htouhui.pdl.util;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
@Component
public class RedisSessionDAO extends CachingSessionDAO {

    private static final String KEY_PREFIX = "hxiaofei.session.";

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisSessionDAO(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doUpdate(Session session) {
        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(KEY_PREFIX + session.getId());
        operations.set(session);
    }

    @Override
    protected void doDelete(Session session) {
        redisTemplate.delete(KEY_PREFIX + session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(KEY_PREFIX + sessionId);
        operations.set(session, session.getTimeout(), TimeUnit.MILLISECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(KEY_PREFIX + sessionId);
        return (Session) operations.get();
    }
}
