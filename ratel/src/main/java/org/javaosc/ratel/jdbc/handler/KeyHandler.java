
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.ratel.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class KeyHandler<K> extends AbstractKeyHandler<K, Map<String, Object>> {

    
    protected final RowConvert convert;

    
    protected final int columnIndex;

    
    protected final String columnName;

    
    public KeyHandler() {
        this(ArrayHandler.ROW_PROCESSOR, 1, null);
    }

    
    public KeyHandler(RowConvert convert) {
        this(convert, 1, null);
    }

    
    public KeyHandler(int columnIndex) {
        this(ArrayHandler.ROW_PROCESSOR, columnIndex, null);
    }

    
    public KeyHandler(String columnName) {
        this(ArrayHandler.ROW_PROCESSOR, 1, columnName);
    }

    
    private KeyHandler(RowConvert convert, int columnIndex,
            String columnName) {
        super();
        this.convert = convert;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        return (columnName == null) ?
               (K) rs.getObject(columnIndex) :
               (K) rs.getObject(columnName);
    }

    
    @Override
    protected Map<String, Object> createRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
