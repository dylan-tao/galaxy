
package org.javaosc.framework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class AsyncJdbcHandler extends AbstractJdbcHandler {

    private final ExecutorService executorService;
    private final JdbcHandler jdbcHandler;

    
    public AsyncJdbcHandler(ExecutorService executorService, JdbcHandler jdbcHandler) {
        this.executorService = executorService;
        this.jdbcHandler = jdbcHandler;
    }
    
    public AsyncJdbcHandler(ExecutorService executorService) {
        this(false, executorService);
    }

    public AsyncJdbcHandler(boolean pmdKnownBroken, ExecutorService executorService) {
        super(pmdKnownBroken);
        this.executorService = executorService;
        this.jdbcHandler = new JdbcHandler(pmdKnownBroken);
    }

    
    public Future<int[]> batch(final Connection conn, final String sql, final Object[][] params) throws SQLException {
        return executorService.submit(new Callable<int[]>() {

            @Override
            public int[] call() throws Exception {
                return jdbcHandler.batch(conn, sql, params);
            }

        });
    }
    
    protected class QueryCallableStatement<T> implements Callable<T> {
        private final String sql;
        private final Object[] params;
        private final Connection conn;
        private final boolean closeConn;
        private final PreparedStatement ps;
        private final ResultSetHandler<T> rsh;

        
        public QueryCallableStatement(Connection conn, boolean closeConn, PreparedStatement ps,
                ResultSetHandler<T> rsh, String sql, Object... params) {
            this.sql = sql;
            this.params = params;
            this.conn = conn;
            this.closeConn = closeConn;
            this.ps = ps;
            this.rsh = rsh;
        }

        
        @Override
        public T call() throws SQLException {
            ResultSet rs = null;
            T ret = null;

            try {
                rs = wrap(ps.executeQuery());
                ret = rsh.handle(rs);
            } catch (SQLException e) {
                rethrow(e, sql, params);
            } finally {
                try {
                    close(rs);
                } finally {
                    close(ps);
                    if (closeConn) {
                        close(conn);
                    }
                }
            }

            return ret;
        }

    }

    
    public <T> Future<T> query(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object... params)
            throws SQLException {
        return executorService.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return jdbcHandler.query(conn, sql, rsh, params);
            }

        });
    }

    
    public <T> Future<T> query(final Connection conn, final String sql, final ResultSetHandler<T> rsh) throws SQLException {
        return executorService.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return jdbcHandler.query(conn, sql, rsh);
            }

        });
    }
    
    public Future<Integer> update(final Connection conn, final String sql) throws SQLException {
        return executorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(jdbcHandler.update(conn, sql));
            }

        });
    }

    
    public Future<Integer> update(final Connection conn, final String sql, final Object param) throws SQLException {
        return executorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(jdbcHandler.update(conn, sql, param));
            }

        });
    }

    
    public Future<Integer> update(final Connection conn, final String sql, final Object... params) throws SQLException {
        return executorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(jdbcHandler.update(conn, sql, params));
            }

        });
    }
    
    
    public <T> Future<T> insert(final Connection conn, final String sql, final ResultSetHandler<T> rsh) throws SQLException {
        return executorService.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return jdbcHandler.insert(conn, sql, rsh);
            }
        });
    }

    
    public <T> Future<T> insert(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object... params) throws SQLException {
        return executorService.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return jdbcHandler.insert(conn, sql, rsh, params);
            }
        });
    }

    public <T> Future<T> insertBatch(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object[][] params) throws SQLException {
        return executorService.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return jdbcHandler.insertBatch(conn, sql, rsh, params);
            }
        });
    }

}
