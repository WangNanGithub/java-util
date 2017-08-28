package com.htouhui.pdl.util;

import com.htouhui.pdl.user.UserAgent;

/**
 * @author hxc，xiongchao.hao@htouhui.com
 * @date 2017/8/17.
 */
public class UserAgentUtils {
    private static final ThreadLocal<UserAgent> localUserAgent = new ThreadLocal<>();

    public static UserAgent getCurrent() {
        return localUserAgent.get();
    }

    public static void setUserAgent(UserAgent userAgent) {
        localUserAgent.set(userAgent);
    }

    public static void reset() {
        localUserAgent.remove();
    }
}
