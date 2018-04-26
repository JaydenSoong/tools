package com.soong.common;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;
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

    /**
     * 把 Map 转换成指定类型的 javaBean 对象。
     * @param map，需要转换的 map
     * @param clazz，将要转换成的类型
     * @param <T>，泛型
     * @return 转换完成的 javaBean
     */
    public static <T> T toBean(Map map, Class<T> clazz) {
        try {
            // 1. 创建指定类型的 javaBean 对象
            T bean = clazz.newInstance();
            // 2. 把数据封装到 javaBean 中
            BeanUtils.populate(bean, map);
            // 3. 返回封装好的 javaBean 对象
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
