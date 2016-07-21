
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.framework.jdbc.core.RowProcessor;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanMapHandler<K, V> extends AbstractKeyedHandler<K, V> {

    
    private final Class<V> type;

    
    private final RowProcessor convert;

    
    private final int columnIndex;

    
    private final String columnName;

    
    public BeanMapHandler(Class<V> type) {
        this(type, ArrayHandler.ROW_PROCESSOR, 1, null);
    }

    
    public BeanMapHandler(Class<V> type, RowProcessor convert) {
        this(type, convert, 1, null);
    }

    
    public BeanMapHandler(Class<V> type, int columnIndex) {
        this(type, ArrayHandler.ROW_PROCESSOR, columnIndex, null);
    }

    
    public BeanMapHandler(Class<V> type, String columnName) {
        this(type, ArrayHandler.ROW_PROCESSOR, 1, columnName);
    }

    
    private BeanMapHandler(Class<V> type, RowProcessor convert,
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
