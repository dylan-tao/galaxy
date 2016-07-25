
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.framework.jdbc.core.RowAssist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class KeyedHandler<K> extends AbstractKeyedHandler<K, Map<String, Object>> {

    
    protected final RowAssist convert;

    
    protected final int columnIndex;

    
    protected final String columnName;

    
    public KeyedHandler() {
        this(ArrayHandler.ROW_PROCESSOR, 1, null);
    }

    
    public KeyedHandler(RowAssist convert) {
        this(convert, 1, null);
    }

    
    public KeyedHandler(int columnIndex) {
        this(ArrayHandler.ROW_PROCESSOR, columnIndex, null);
    }

    
    public KeyedHandler(String columnName) {
        this(ArrayHandler.ROW_PROCESSOR, 1, columnName);
    }

    
    private KeyedHandler(RowAssist convert, int columnIndex,
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
