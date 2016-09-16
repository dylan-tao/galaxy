
package org.javaosc.ratel.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.ratel.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanMapType<K, V> extends AbstractKeyType<K, V> {

    
    private final Class<V> type;

    
    private final RowConvert convert;

    
    private final int columnIndex;

    
    private final String columnName;

    
    public BeanMapType(Class<V> type) {
        this(type, ArrayType.ROW_PROCESSOR, 1, null);
    }

    
    public BeanMapType(Class<V> type, RowConvert convert) {
        this(type, convert, 1, null);
    }

    
    public BeanMapType(Class<V> type, int columnIndex) {
        this(type, ArrayType.ROW_PROCESSOR, columnIndex, null);
    }

    
    public BeanMapType(Class<V> type, String columnName) {
        this(type, ArrayType.ROW_PROCESSOR, 1, columnName);
    }

    
    private BeanMapType(Class<V> type, RowConvert convert,
            int columnIndex, String columnName) {
        super();
        this.type = type;
        this.convert = convert;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    @SuppressWarnings("unchecked")
    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        return (columnName == null) ?
               (K) rs.getObject(columnIndex) :
               (K) rs.getObject(columnName);
    }

    @Override
    protected V createRow(ResultSet rs) throws SQLException {
        return this.convert.toBean(rs, type);
    }

}
