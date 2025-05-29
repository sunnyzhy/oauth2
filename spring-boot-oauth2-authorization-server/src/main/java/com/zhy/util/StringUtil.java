package com.zhy.util;

import java.util.UUID;

/**
 * @author zhy
 * @date 2025/5/27 11:57
 */
public class StringUtil {
    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static String uuidTrim() {
        String uuidStr = uuid();
        uuidStr = uuidStr.replaceAll("-", "");
        return uuidStr;
    }
}
