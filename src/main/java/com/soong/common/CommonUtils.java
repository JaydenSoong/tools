package com.soong.common;

import java.util.UUID;

/**
 * 自定义工具类
 */
public class CommonUtils {
    /**
     * @return 返回一个不重复的字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
