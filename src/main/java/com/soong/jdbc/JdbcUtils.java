package com.soong.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
	private static Properties props;
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

	// 支持多线程和事务的专用连接
	private static ThreadLocal<Connection> tl = new ThreadLocal<>();

	/**
	 * 使用 c3p0 的方式获取连接，需要提供 c3p0-config.xml
	 */
	public static Connection getConnection() throws SQLException {
		// 如果能够从 tl 中获取到 conn，说明开启了事物，返回事物专用连接
		Connection conn = tl.get();
		if (conn != null) return conn;

		// 返回普通连接
		return dataSource.getConnection();
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
	public static void beginTransaction() throws SQLException {
		Connection conn = tl.get();
		if (conn != null) throw new RuntimeException("事务已经开启");
		conn = dataSource.getConnection();
		conn.setAutoCommit(false);
		// 保存事物专用连接到 ThreadLocal 中
		tl.set(conn);
	}

	/**
	 * 提交事物
	 */
	public static void commintTransaction() throws SQLException {
		Connection conn = tl.get();
		if (conn == null) throw new RuntimeException("事务还未开启");
		conn.commit(); // 提交事务
		conn.close(); // 关闭连接
		conn = null; // 表示事务结束
		tl.remove();
	}

	/**
	 * 回滚事物
	 */
	public static void rollbackTransaction() throws SQLException {
		Connection conn = tl.get();
		if (conn == null) throw new RuntimeException("事务还未开启");
		conn.rollback();
		conn.close();
		conn = null;
		tl.remove();
	}

	/**
	 * 释放连接
	 * @param connection, 需要被释放的连接
	 */
	public static void releaseConnection(Connection connection) throws SQLException {
		Connection conn = tl.get();
		/*
		 * 1. 没有从 ThreadLocal 中获取到连接，说明事物还未开启，传入的连接将在这里被关闭
		 * 2. 传入的连接与 ThreadLocal 中的连接不是同一个，传入的连接将在这里被关闭
		 */
		if(connection != conn) { // 如果参数连接，与当前事务连接不同，说明这个连接不是当前事务，可以关闭
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
}
