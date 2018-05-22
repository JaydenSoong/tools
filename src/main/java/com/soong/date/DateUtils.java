package com.soong.date;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class DateUtils {
    /**
     * 将 java.util.date 转换成 java.sql.Date
     */
    public static Date toSqlDate(java.util.Date date) {
        return new Date(date.getTime());
    }

    /**
     * 将 java.util.date 转换成 java.sql.Time
     */
    public static Time toSqlTime(java.util.Date date) {
        return new Time(date.getTime());
    }

    /**
     * 将 java.util.date 转换成 java.sql.Timestamp
     */
    public static Timestamp toTimestamp(java.util.Date date) {
        return new Timestamp(date.getTime());
    }
}
