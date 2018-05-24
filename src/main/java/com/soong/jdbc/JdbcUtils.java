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

}
