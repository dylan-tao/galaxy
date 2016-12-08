
package org.javaosc.galaxy.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.javaosc.galaxy.util.StringUtil;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class JdbcHandler extends JdbcSuperHandler {
	
	public JdbcHandler(){}
    
    public JdbcHandler(boolean sqlPrepareCheck) {
        super(sqlPrepareCheck);
    }

	public int[] batch(Connection conn, String sql, Object[][] params) throws SQLException {
        return this.batch(conn, false, sql, params);
    }

    private int[] batch(Connection conn, boolean closeConn, String sql, Object[][] params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Data connection cannot be null");
        }
        
        if (StringUtil.isBlank(sql)) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Sql cannot be null");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Parameters can not be empty, if you need to affect the  data rows, please pass an empty array");
        }

        PreparedStatement stmt = null;
        int[] rows = null;
        try {
            stmt = this.prepareStatement(conn, sql);
            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();
        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[])params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    
    public <T> T query(Connection conn, String sql, ResultType<T> rt, Object... params) throws SQLException {
        return this.<T>query(conn, false, sql, rt, params);
    }

    
    public <T> T query(Connection conn, String sql, ResultType<T> rt) throws SQLException {
        return this.<T>query(conn, false, sql, rt, (Object[]) null);
    }

    
    private <T> T query(Connection conn, boolean closeConn, String sql, ResultType<T> rt, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Data connection cannot be null");
        }

        if (StringUtil.isBlank(sql)) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Sql cannot be null");
        }

        if (rt == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("ResultType cannot be null");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rt.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
                if (closeConn) {
                    close(conn);
                }
            }
        }

        return result;
    }

    
    public int update(Connection conn, String sql) throws SQLException {
        return this.update(conn, false, sql, (Object[]) null);
    }

    
    public int update(Connection conn, String sql, Object param) throws SQLException {
        return this.update(conn, false, sql, new Object[]{param});
    }

    
    public int update(Connection conn, String sql, Object... params) throws SQLException {
        return update(conn, false, sql, params);
    }

    private int update(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Data connection cannot be null");
        }

        if (StringUtil.isBlank(sql)) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Sql cannot be null");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    public <T> T insert(Connection conn, String sql, ResultType<T> rt) throws SQLException {
        return insert(conn, false, sql, rt, (Object[]) null);
    }

    
    public <T> T insert(Connection conn, String sql, ResultType<T> rt, Object... params) throws SQLException {
        return insert(conn, false, sql, rt, params);
    }

    
    private <T> T insert(Connection conn, boolean closeConn, String sql, ResultType<T> rt, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Data connection cannot be null");
        }

        if (StringUtil.isBlank(sql)) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Sql cannot be null");
        }

        if (rt == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("ResultType cannot be null");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            generatedKeys = rt.handle(resultSet);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }

    public <T> T insertBatch(Connection conn, String sql, ResultType<T> rt, Object[][] params) throws SQLException {
        return insertBatch(conn, false, sql, rt, params);
    }
    
    private <T> T insertBatch(Connection conn, boolean closeConn, String sql, ResultType<T> rt, Object[][] params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Data connection cannot be null");
        }

        if (StringUtil.isBlank(sql)) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Sql cannot be null");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Parameters can not be empty, if you need to affect the  data rows, please pass an empty array");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;
        try {
            stmt = this.prepareStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            ResultSet rs = stmt.getGeneratedKeys();
            generatedKeys = rt.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[])params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }
}
