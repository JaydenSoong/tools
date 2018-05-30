package com.soong.dbutils;

import com.soong.jdbc.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 对 dbutils 的 QueryRunner 进行包装，重写其不带 Connection 的方法
 * 重写思路：1. 获取连接
 *           2. 调用父类方法得到结果
 *           3. 释放连接
 *           4. 返回结果
 */
public class TxQueryRunner extends QueryRunner {
    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int[] result = super.batch(conn, sql, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        T result = super.query(conn, sql, rsh, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        T result = super.query(conn, sql, rsh);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public int update(String sql) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int result = super.update(conn, sql);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public int update(String sql, Object param) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int result = super.update(conn, sql, param);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public int update(String sql, Object... params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int result = super.update(conn, sql, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> T insert(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        T result = super.insert(conn, sql, rsh);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        T result = super.insert(conn, sql, rsh, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][] params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        T result = super.insertBatch(conn, sql, rsh, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public int execute(String sql, Object... params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int result = super.execute(conn, sql, params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }

    @Override
    public <T> List<T> execute(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        List<T> result = super.execute(conn, sql, rsh,params);
        JdbcUtils.releaseConnection(conn);
        return  result;
    }
}
