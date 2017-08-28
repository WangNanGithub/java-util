/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with haitouhui.com.
 */
package com.htouhui.pdl.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
public class DingDingSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingDingSender.class);

    public static void sendTextMessage(String message, String robotURL) {
        String jsonParams = "{" +
                "\"msgtype\":\"text\"," +
                "\"text\":{" +
                "  \"content\":\"" + message + "\"" +
                "}," +
                "\"at\":{" +
                "  \"isAtAll\":true" +
                "}}";
        doSend(jsonParams, robotURL);
    }

    public static void sendMarkdownMessage(String title, String message, String robotURL) {
        String requestBody = "{" +
                "\"msgtype\": \"markdown\"," +
                "\"markdown\": " +
                "{" +
                "  \"title\":\"" + title + "\"," +
                "  \"text\": \"" + message + "\"" +
                "  }" +
                "}";
        doSend(requestBody, robotURL);
    }

    private static void doSend(String message, String robotURL) {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(robotURL);
            httpPost.setHeader("charset", "UTF-8");
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(message, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse res = client.execute(httpPost);
            int status = res.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                LOGGER.error("钉钉还借钱机器人请求出现异常，状态码为：" + status);
            }
            HttpEntity entityRes = res.getEntity();
            String s = EntityUtils.toString(entityRes, "UTF-8");
            LOGGER.info("钉钉还借钱机器人请求响应内容:{}", s);
        } catch (Exception e) {
            LOGGER.error("钉钉还借钱机器人请求抛锚Exception : ", e);
        } finally {
            httpPost.releaseConnection();
        }
    }
}
