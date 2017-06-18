
package org.javaosc.galaxy.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.galaxy.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class KeyType<K> extends AbstractKeyType<K, Map<String, Object>> {

    
    protected final RowConvert convert;

    
    protected final int columnIndex;

    
    protected final String columnName;

    
    public KeyType() {
        this(ArrayType.ROW_PROCESSOR, 1, null);
    }

    
    public KeyType(RowConvert convert) {
        this(convert, 1, null);
    }

    
    public KeyType(int columnIndex) {
        this(ArrayType.ROW_PROCESSOR, columnIndex, null);
    }

    
    public KeyType(String columnName) {
        this(ArrayType.ROW_PROCESSOR, 1, columnName);
    }

    
    private KeyType(RowConvert convert, int columnIndex,
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
