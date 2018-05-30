package com.soong.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static Properties props;
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    // 支持多线程和事务的专用连接
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    // 静态代码块完成配置文件的加载和驱动的加载。这里使用静态代码块可以保证只加载一次
    // 废弃方法 getConnectionNoPool 使用
    static {
        // 对 props 进行初始化，即加载配置文件
        try {
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
            props = new Properties();
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 加载驱动类
        try {
            Class.forName(props.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用原始的方式获取数据库连接
     * 需要提供四大连接参数
     */
    @Deprecated
    public static Connection getConnectionNoPool() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("url"),
                props.getProperty("username"),
                props.getProperty("password"));
    }

    /**
     * 使用 c3p0 的方式获取连接，需要提供 c3p0-config.xml
     */
    public static Connection getConnection() {
        // 如果能够从 tl 中获取到 conn，说明开启了事物，返回事物专用连接
        Connection conn = tl.get();
        if (conn != null) return conn;

        // 返回普通连接
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 c3p0 的方式获取 DataSource，需要提供 c3p0-config.xml
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 开启事物
     */
    public static void beginTransaction() {
        Connection conn = tl.get();
        if (conn != null) throw new RuntimeException("事务已经开启");
        conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 保存事物专用连接到 ThreadLocal 中
        tl.set(conn);
    }

    /**
     * 提交事物
     */
    public static void commintTransaction() {
        Connection conn = tl.get();
        if (conn == null) throw new RuntimeException("事务还未开启");
        try {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tl.remove();
    }

    /**
     * 回滚事物
     */
    public static void rollbackTransaction() {
        Connection conn = tl.get();
        if (conn == null) throw new RuntimeException("事务还未开启");
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tl.remove();
    }

    /**
     * 释放连接
     * @param connection, 需要被释放的连接
     */
    public static void releaseConnection(Connection connection) {
        Connection conn = tl.get();
        /*
         * 1. 没有从 ThreadLocal 中获取到连接，说明事物还未开启，传入的连接将在这里被关闭
         * 2. 传入的连接与 ThreadLocal 中的连接不是同一个，传入的连接将在这里被关闭
         */
        if (conn == null || conn != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
