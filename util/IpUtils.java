/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */

package com.htouhui.pdl.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 参考：https://imququ.com/post/x-forwarded-for-header-in-http.html
 * <p>
 * 使用 Nginx 等 Web Server 进行反向代理的 Web 应用，在配置正确的前提下，要用 X-Forwarded-For 最后一节 或 X-Real-IP 来获取 IP（因为 Remote Address 得到的是
 * Nginx 所在服务器的内网 IP）；同时还应该禁止 Web 应用直接对外提供服务；
 * <p>
 *     <pre>
 *         location / {
 *             proxy_set_header X-Real-IP $remote_addr;
 *             proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
 *             proxy_set_header Host $http_host;
 *             proxy_set_header X-NginX-Proxy true;
 *
 *             proxy_pass http://127.0.0.1:9009/;
 *             proxy_redirect off;
 *         }
 *     </pre>
 * </p>
 *
 * @author hxc，xiongchao.hao@htouhui.com
 * @version 1.0
 */
public final class IpUtils {
    private IpUtils() {
    }

    public static String getIp(HttpServletRequest request) {

        String ip = StringUtils.trimToEmpty(request.getHeader("X-Real-IP"));
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(ip) &&
                StringUtils.contains(ip, ',')) {
                ip = StringUtils.substringAfterLast(ip, ",");
                ip = StringUtils.trimToEmpty(ip);
            }
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")? "127.0.0.1" : ip;
    }
}
