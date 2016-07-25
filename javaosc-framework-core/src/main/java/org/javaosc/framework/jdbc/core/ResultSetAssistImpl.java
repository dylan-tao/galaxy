
package org.javaosc.framework.jdbc.core;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public abstract class ResultSetAssistImpl<T> implements ResultSetAssist<T> {

    
    private ResultSet rs;

    
    @Override
    public final T handle(ResultSet rs) throws SQLException {
        if (this.rs != null) {
            throw new IllegalStateException("Re-entry not allowed!");
        }

        this.rs = rs;

        try {
            return handle();
        } finally {
            this.rs = null;
        }
    }

    
    protected abstract T handle() throws SQLException;

    
    protected final boolean absolute(int row) throws SQLException {
        return rs.absolute(row);
    }

    
    protected final void afterLast() throws SQLException {
        rs.afterLast();
    }

    
    protected final void beforeFirst() throws SQLException {
        rs.beforeFirst();
    }

    
    protected final void cancelRowUpdates() throws SQLException {
        rs.cancelRowUpdates();
    }

    
    protected final void clearWarnings() throws SQLException {
        rs.clearWarnings();
    }

    
    protected final void close() throws SQLException {
        rs.close();
    }

    
    protected final void deleteRow() throws SQLException {
        rs.deleteRow();
    }

    
    protected final int findColumn(String columnLabel) throws SQLException {
        return rs.findColumn(columnLabel);
    }

    
    protected final boolean first() throws SQLException {
        return rs.first();
    }

    
    protected final Array getArray(int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }

    
    protected final Array getArray(String columnLabel) throws SQLException {
        return rs.getArray(columnLabel);
    }

    
    protected final InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs.getAsciiStream(columnIndex);
    }

    
    protected final InputStream getAsciiStream(String columnLabel) throws SQLException {
        return rs.getAsciiStream(columnLabel);
    }

    protected final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }
    
    protected final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel);
    }

    
    protected final InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    
    protected final InputStream getBinaryStream(String columnLabel) throws SQLException {
        return rs.getBinaryStream(columnLabel);
    }

    
    protected final Blob getBlob(int columnIndex) throws SQLException {
        return rs.getBlob(columnIndex);
    }

    
    protected final Blob getBlob(String columnLabel) throws SQLException {
        return rs.getBlob(columnLabel);
    }

    
    protected final boolean getBoolean(int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    
    protected final boolean getBoolean(String columnLabel) throws SQLException {
        return rs.getBoolean(columnLabel);
    }

    
    protected final byte getByte(int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }

    
    protected final byte getByte(String columnLabel) throws SQLException {
        return rs.getByte(columnLabel);
    }

    
    protected final byte[] getBytes(int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }

    
    protected final byte[] getBytes(String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }

    
    protected final Reader getCharacterStream(int columnIndex) throws SQLException {
        return rs.getCharacterStream(columnIndex);
    }

    
    protected final Reader getCharacterStream(String columnLabel) throws SQLException {
        return rs.getCharacterStream(columnLabel);
    }

    
    protected final Clob getClob(int columnIndex) throws SQLException {
        return rs.getClob(columnIndex);
    }

    
    protected final Clob getClob(String columnLabel) throws SQLException {
        return rs.getClob(columnLabel);
    }

    
    protected final int getConcurrency() throws SQLException {
        return rs.getConcurrency();
    }

    
    protected final String getCursorName() throws SQLException {
        return rs.getCursorName();
    }

    
    protected final Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return rs.getDate(columnIndex, cal);
    }

    
    protected final Date getDate(int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    
    protected final Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return rs.getDate(columnLabel, cal);
    }

    
    protected final Date getDate(String columnLabel) throws SQLException {
        return rs.getDate(columnLabel);
    }

    
    protected final double getDouble(int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }

    
    protected final double getDouble(String columnLabel) throws SQLException {
        return rs.getDouble(columnLabel);
    }

    
    protected final int getFetchDirection() throws SQLException {
        return rs.getFetchDirection();
    }

    
    protected final int getFetchSize() throws SQLException {
        return rs.getFetchSize();
    }

    
    protected final float getFloat(int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }

    
    protected final float getFloat(String columnLabel) throws SQLException {
        return rs.getFloat(columnLabel);
    }

    
    protected final int getHoldability() throws SQLException {
        return rs.getHoldability();
    }

    
    protected final int getInt(int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    
    protected final int getInt(String columnLabel) throws SQLException {
        return rs.getInt(columnLabel);
    }

    
    protected final long getLong(int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }

    
    protected final long getLong(String columnLabel) throws SQLException {
        return rs.getLong(columnLabel);
    }

    
    protected final ResultSetMetaData getMetaData() throws SQLException {
        return rs.getMetaData();
    }

    
    protected final Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs.getNCharacterStream(columnIndex);
    }

    
    protected final Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs.getNCharacterStream(columnLabel);
    }

    
    protected final NClob getNClob(int columnIndex) throws SQLException {
        return rs.getNClob(columnIndex);
    }

    
    protected final NClob getNClob(String columnLabel) throws SQLException {
        return rs.getNClob(columnLabel);
    }

    
    protected final String getNString(int columnIndex) throws SQLException {
        return rs.getNString(columnIndex);
    }

    
    protected final String getNString(String columnLabel) throws SQLException {
        return rs.getNString(columnLabel);
    }

    
    protected final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnIndex, map);
    }

    
    protected final Object getObject(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    
    protected final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnLabel, map);
    }

    
    protected final Object getObject(String columnLabel) throws SQLException {
        return rs.getObject(columnLabel);
    }

    
    protected final Ref getRef(int columnIndex) throws SQLException {
        return rs.getRef(columnIndex);
    }

    
    protected final Ref getRef(String columnLabel) throws SQLException {
        return rs.getRef(columnLabel);
    }

    
    protected final int getRow() throws SQLException {
        return rs.getRow();
    }

    
    protected final RowId getRowId(int columnIndex) throws SQLException {
        return rs.getRowId(columnIndex);
    }

    
    protected final RowId getRowId(String columnLabel) throws SQLException {
        return rs.getRowId(columnLabel);
    }

    
    protected final SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }

    
    protected final SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs.getSQLXML(columnLabel);
    }

    
    protected final short getShort(int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }

    
    protected final short getShort(String columnLabel) throws SQLException {
        return rs.getShort(columnLabel);
    }

    
    protected final Statement getStatement() throws SQLException {
        return rs.getStatement();
    }

    
    protected final String getString(int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    
    protected final String getString(String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    
    protected final Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTime(columnIndex, cal);
    }

    
    protected final Time getTime(int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }

    
    protected final Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTime(columnLabel, cal);
    }

    
    protected final Time getTime(String columnLabel) throws SQLException {
        return rs.getTime(columnLabel);
    }

    
    protected final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTimestamp(columnIndex, cal);
    }

    
    protected final Timestamp getTimestamp(int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }

    
    protected final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTimestamp(columnLabel, cal);
    }

    
    protected final Timestamp getTimestamp(String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }

    
    protected final int getType() throws SQLException {
        return rs.getType();
    }

    
    protected final URL getURL(int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }

    
    protected final URL getURL(String columnLabel) throws SQLException {
        return rs.getURL(columnLabel);
    }
    
    protected final SQLWarning getWarnings() throws SQLException {
        return rs.getWarnings();
    }

    
    protected final void insertRow() throws SQLException {
        rs.insertRow();
    }

    
    protected final boolean isAfterLast() throws SQLException {
        return rs.isAfterLast();
    }

    
    protected final boolean isBeforeFirst() throws SQLException {
        return rs.isBeforeFirst();
    }

    
    protected final boolean isClosed() throws SQLException {
        return rs.isClosed();
    }

    
    protected final boolean isFirst() throws SQLException {
        return rs.isFirst();
    }

    
    protected final boolean isLast() throws SQLException {
        return rs.isLast();
    }

    
    protected final boolean isWrapperFor(Class<?> iface) throws SQLException {
        return rs.isWrapperFor(iface);
    }

    
    protected final boolean last() throws SQLException {
        return rs.last();
    }

    
    protected final void moveToCurrentRow() throws SQLException {
        rs.moveToCurrentRow();
    }

    
    protected final void moveToInsertRow() throws SQLException {
        rs.moveToInsertRow();
    }

    
    protected final boolean next() throws SQLException {
        return rs.next();
    }

    
    protected final boolean previous() throws SQLException {
        return rs.previous();
    }

    
    protected final void refreshRow() throws SQLException {
        rs.refreshRow();
    }

    
    protected final boolean relative(int rows) throws SQLException {
        return rs.relative(rows);
    }

    
    protected final boolean rowDeleted() throws SQLException {
        return rs.rowDeleted();
    }

    
    protected final boolean rowInserted() throws SQLException {
        return rs.rowInserted();
    }

    
    protected final boolean rowUpdated() throws SQLException {
        return rs.rowUpdated();
    }

    
    protected final void setFetchDirection(int direction) throws SQLException {
        rs.setFetchDirection(direction);
    }

    
    protected final void setFetchSize(int rows) throws SQLException {
        rs.setFetchSize(rows);
    }

    
    protected final <E> E unwrap(Class<E> iface) throws SQLException {
        return rs.unwrap(iface);
    }

    
    protected final void updateArray(int columnIndex, Array x) throws SQLException {
        rs.updateArray(columnIndex, x);
    }

    
    protected final void updateArray(String columnLabel, Array x) throws SQLException {
        rs.updateArray(columnLabel, x);
    }

    
    protected final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        rs.updateAsciiStream(columnIndex, x, length);
    }

    
    protected final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        rs.updateAsciiStream(columnIndex, x, length);
    }

    
    protected final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        rs.updateAsciiStream(columnIndex, x);
    }

    
    protected final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        rs.updateAsciiStream(columnLabel, x, length);
    }

    
    protected final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        rs.updateAsciiStream(columnLabel, x, length);
    }

    
    protected final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        rs.updateAsciiStream(columnLabel, x);
    }

    
    protected final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        rs.updateBigDecimal(columnIndex, x);
    }

    
    protected final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        rs.updateBigDecimal(columnLabel, x);
    }

    
    protected final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        rs.updateBinaryStream(columnIndex, x, length);
    }

    
    protected final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        rs.updateBinaryStream(columnIndex, x, length);
    }

    
    protected final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        rs.updateBinaryStream(columnIndex, x);
    }

    
    protected final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        rs.updateBinaryStream(columnLabel, x, length);
    }

    
    protected final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        rs.updateBinaryStream(columnLabel, x, length);
    }

    
    protected final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        rs.updateBinaryStream(columnLabel, x);
    }

    
    protected final void updateBlob(int columnIndex, Blob x) throws SQLException {
        rs.updateBlob(columnIndex, x);
    }

    
    protected final void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        rs.updateBlob(columnIndex, inputStream, length);
    }

    
    protected final void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        rs.updateBlob(columnIndex, inputStream);
    }

    
    protected final void updateBlob(String columnLabel, Blob x) throws SQLException {
        rs.updateBlob(columnLabel, x);
    }

    
    protected final void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        rs.updateBlob(columnLabel, inputStream, length);
    }

    
    protected final void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        rs.updateBlob(columnLabel, inputStream);
    }

    
    protected final void updateBoolean(int columnIndex, boolean x) throws SQLException {
        rs.updateBoolean(columnIndex, x);
    }

    
    protected final void updateBoolean(String columnLabel, boolean x) throws SQLException {
        rs.updateBoolean(columnLabel, x);
    }

    
    protected final void updateByte(int columnIndex, byte x) throws SQLException {
        rs.updateByte(columnIndex, x);
    }

    
    protected final void updateByte(String columnLabel, byte x) throws SQLException {
        rs.updateByte(columnLabel, x);
    }

    
    protected final void updateBytes(int columnIndex, byte[] x) throws SQLException {
        rs.updateBytes(columnIndex, x);
    }

    
    protected final void updateBytes(String columnLabel, byte[] x) throws SQLException {
        rs.updateBytes(columnLabel, x);
    }

    
    protected final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        rs.updateCharacterStream(columnIndex, x, length);
    }

    
    protected final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        rs.updateCharacterStream(columnIndex, x, length);
    }

    
    protected final void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        rs.updateCharacterStream(columnIndex, x);
    }

    
    protected final void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        rs.updateCharacterStream(columnLabel, reader, length);
    }

    
    protected final void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        rs.updateCharacterStream(columnLabel, reader, length);
    }

    
    protected final void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        rs.updateCharacterStream(columnLabel, reader);
    }

    
    protected final void updateClob(int columnIndex, Clob x) throws SQLException {
        rs.updateClob(columnIndex, x);
    }

    
    protected final void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        rs.updateClob(columnIndex, reader, length);
    }

    
    protected final void updateClob(int columnIndex, Reader reader) throws SQLException {
        rs.updateClob(columnIndex, reader);
    }

    
    protected final void updateClob(String columnLabel, Clob x) throws SQLException {
        rs.updateClob(columnLabel, x);
    }

    
    protected final void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        rs.updateClob(columnLabel, reader, length);
    }

    
    protected final void updateClob(String columnLabel, Reader reader) throws SQLException {
        rs.updateClob(columnLabel, reader);
    }

    
    protected final void updateDate(int columnIndex, Date x) throws SQLException {
        rs.updateDate(columnIndex, x);
    }

    
    protected final void updateDate(String columnLabel, Date x) throws SQLException {
        rs.updateDate(columnLabel, x);
    }

    
    protected final void updateDouble(int columnIndex, double x) throws SQLException {
        rs.updateDouble(columnIndex, x);
    }

    
    protected final void updateDouble(String columnLabel, double x) throws SQLException {
        rs.updateDouble(columnLabel, x);
    }

    
    protected final void updateFloat(int columnIndex, float x) throws SQLException {
        rs.updateFloat(columnIndex, x);
    }

    
    protected final void updateFloat(String columnLabel, float x) throws SQLException {
        rs.updateFloat(columnLabel, x);
    }

    
    protected final void updateInt(int columnIndex, int x) throws SQLException {
        rs.updateInt(columnIndex, x);
    }

    
    protected final void updateInt(String columnLabel, int x) throws SQLException {
        rs.updateInt(columnLabel, x);
    }

    
    protected final void updateLong(int columnIndex, long x) throws SQLException {
        rs.updateLong(columnIndex, x);
    }

    
    protected final void updateLong(String columnLabel, long x) throws SQLException {
        rs.updateLong(columnLabel, x);
    }

    
    protected final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        rs.updateNCharacterStream(columnIndex, x, length);
    }

    
    protected final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        rs.updateNCharacterStream(columnIndex, x);
    }

    
    protected final void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        rs.updateNCharacterStream(columnLabel, reader, length);
    }

    
    protected final void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        rs.updateNCharacterStream(columnLabel, reader);
    }

    
    protected final void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        rs.updateNClob(columnIndex, nClob);
    }

    
    protected final void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        rs.updateNClob(columnIndex, reader, length);
    }

    
    protected final void updateNClob(int columnIndex, Reader reader) throws SQLException {
        rs.updateNClob(columnIndex, reader);
    }

    
    protected final void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        rs.updateNClob(columnLabel, nClob);
    }

    
    protected final void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        rs.updateNClob(columnLabel, reader, length);
    }

    
    protected final void updateNClob(String columnLabel, Reader reader) throws SQLException {
        rs.updateNClob(columnLabel, reader);
    }

    
    protected final void updateNString(int columnIndex, String nString) throws SQLException {
        rs.updateNString(columnIndex, nString);
    }

    
    protected final void updateNString(String columnLabel, String nString) throws SQLException {
        rs.updateNString(columnLabel, nString);
    }

    
    protected final void updateNull(int columnIndex) throws SQLException {
        rs.updateNull(columnIndex);
    }

    
    protected final void updateNull(String columnLabel) throws SQLException {
        rs.updateNull(columnLabel);
    }

    
    protected final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        rs.updateObject(columnIndex, x, scaleOrLength);
    }

    
    protected final void updateObject(int columnIndex, Object x) throws SQLException {
        rs.updateObject(columnIndex, x);
    }

    
    protected final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        rs.updateObject(columnLabel, x, scaleOrLength);
    }

    
    protected final void updateObject(String columnLabel, Object x) throws SQLException {
        rs.updateObject(columnLabel, x);
    }

    
    protected final void updateRef(int columnIndex, Ref x) throws SQLException {
        rs.updateRef(columnIndex, x);
    }

    
    protected final void updateRef(String columnLabel, Ref x) throws SQLException {
        rs.updateRef(columnLabel, x);
    }

    
    protected final void updateRow() throws SQLException {
        rs.updateRow();
    }

    
    protected final void updateRowId(int columnIndex, RowId x) throws SQLException {
        rs.updateRowId(columnIndex, x);
    }

    
    protected final void updateRowId(String columnLabel, RowId x) throws SQLException {
        rs.updateRowId(columnLabel, x);
    }

    
    protected final void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        rs.updateSQLXML(columnIndex, xmlObject);
    }

    
    protected final void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        rs.updateSQLXML(columnLabel, xmlObject);
    }

    
    protected final void updateShort(int columnIndex, short x) throws SQLException {
        rs.updateShort(columnIndex, x);
    }

    
    protected final void updateShort(String columnLabel, short x) throws SQLException {
        rs.updateShort(columnLabel, x);
    }

    
    protected final void updateString(int columnIndex, String x) throws SQLException {
        rs.updateString(columnIndex, x);
    }

    
    protected final void updateString(String columnLabel, String x) throws SQLException {
        rs.updateString(columnLabel, x);
    }

    
    protected final void updateTime(int columnIndex, Time x) throws SQLException {
        rs.updateTime(columnIndex, x);
    }

    
    protected final void updateTime(String columnLabel, Time x) throws SQLException {
        rs.updateTime(columnLabel, x);
    }

    
    protected final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        rs.updateTimestamp(columnIndex, x);
    }

    
    protected final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        rs.updateTimestamp(columnLabel, x);
    }

    
    protected final boolean wasNull() throws SQLException {
        return rs.wasNull();
    }

    protected final ResultSet getAdaptedResultSet() {
        return rs;
    }

}