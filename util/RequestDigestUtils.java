/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.htouhui.pdl.util;

import com.htouhui.pdl.Constants;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
public class RequestDigestUtils {

    /**
     * 处理请求参数。0.去掉摘要参数, 1.按照参数名称排序，2.拼接参数为name1=value1&name2=value2的形式
     *
     * @param parameterMap 请求参数
     * @return 返回拼接好的参数字符串
     */
    public static String formatParameters(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(Constants.PARAMETER_DIGEST))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> entry.getKey() + "=" + Arrays.stream(entry.getValue()).sorted(String::compareTo).collect(Collectors.joining()))
                .collect(Collectors.joining("&"));
    }

    public static boolean check(String parametersLine, String secretToken, String digest) {
        return digest(parametersLine, secretToken).equalsIgnoreCase(digest);
    }

    public static String digest(String parametersLine, String secretToken) {
        return DigestUtils.md5Hex(secretToken + parametersLine + secretToken);
    }
}
