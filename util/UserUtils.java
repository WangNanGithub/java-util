/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.htouhui.pdl.util;

import com.htouhui.pdl.user.User;

/**
 * @author redstarstar, star.hong@gmail.com
 * @version 1.0
 */
public class UserUtils {

    private static final ThreadLocal<User> localUser = new ThreadLocal<>();

    public static User getCurrent() {
        return localUser.get();
    }

    public static void setUser(User user) {
        localUser.set(user);
    }
}
