/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with haitouhui.com.
 */
package com.htouhui.pdl.util;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
@Component
public class CaptchaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaUtils.class);

    private static final String REDIS_KEY_PREFIX = "H5-CAPTCHA-";

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public CaptchaUtils(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateCaptcha() {

        String token = UUID.randomUUID().toString();
        String redisKey = REDIS_KEY_PREFIX + token;

        Captcha captcha = new Captcha.Builder(162, 35)
                .addText(() -> RandomStringUtils.randomNumeric(4))
                .addNoise()
                .build();

        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(redisKey);
        operations.set(captcha);
        operations.expire(5, TimeUnit.MINUTES);
        return token;
    }

    public void generateImage(String token, HttpServletResponse response) {
        String redisKey = REDIS_KEY_PREFIX + token;
        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(redisKey);
        Captcha captcha = (Captcha) operations.get();
        if (captcha != null) {
            CaptchaServletUtil.writeImage(response, captcha.getImage());
        }
    }

    public boolean verify(String token, String code) {
        boolean isLegal = false;
        String redisKey = REDIS_KEY_PREFIX + token;
        BoundValueOperations<Object, Object> operations = redisTemplate.boundValueOps(redisKey);
        Captcha captcha = (Captcha) operations.getAndSet(null);
        LOGGER.info("captcha|{}|redisKey|{}", captcha, redisKey);
        if (captcha != null && captcha.getAnswer().equals(code)) {
            isLegal = true;
        }
        return isLegal;
    }
}
