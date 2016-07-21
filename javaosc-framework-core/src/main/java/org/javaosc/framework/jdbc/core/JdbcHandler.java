
package org.javaosc.framework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class JdbcHandler extends AbstractJdbcHandler {

    
    public JdbcHandler() {
        super();
    }

    
    public JdbcHandler(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    
    public JdbcHandler(DataSource ds) {
        super(ds);
    }

    
    public JdbcHandler(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    
    public int[] batch(Connection conn, String sql, Object[][] params) throws SQLException {
        return this.batch(conn, false, sql, params);
    }

    
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.batch(conn, true, sql, params);
    }

    
    private int[] batch(Connection conn, boolean closeConn, String sql, Object[][] params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
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

    
    @Deprecated
    public <T> T query(Connection conn, String sql, Object param, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, new Object[]{param});
    }

    
    @Deprecated
    public <T> T query(Connection conn, String sql, Object[] params, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, params);
    }

    
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, params);
    }

    
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, (Object[]) null);
    }

    
    @Deprecated
    public <T> T query(String sql, Object param, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, new Object[]{param});
    }

    
    @Deprecated
    public <T> T query(String sql, Object[] params, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, params);
    }

    
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, params);
    }

    
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, (Object[]) null);
    }

    
    private <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

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

    
    public int update(String sql) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, (Object[]) null);
    }

    
    public int update(String sql, Object param) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, new Object[]{param});
    }

    
    public int update(String sql, Object... params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, params);
    }

    
    private int update(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
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

    
    public <T> T insert(String sql, ResultSetHandler<T> rsh) throws SQLException {
        return insert(this.prepareConnection(), true, sql, rsh, (Object[]) null);
    }

    
    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return insert(this.prepareConnection(), true, sql, rsh, params);
    }

    
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return insert(conn, false, sql, rsh, (Object[]) null);
    }

    
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return insert(conn, false, sql, rsh, params);
    }

    
    private <T> T insert(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(resultSet);
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

    
    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][] params) throws SQLException {
        return insertBatch(this.prepareConnection(), true, sql, rsh, params);
    }

    
    public <T> T insertBatch(Connection conn, String sql, ResultSetHandler<T> rsh, Object[][] params) throws SQLException {
        return insertBatch(conn, false, sql, rsh, params);
    }

    
    private <T> T insertBatch(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object[][] params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
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
            generatedKeys = rsh.handle(rs);

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
