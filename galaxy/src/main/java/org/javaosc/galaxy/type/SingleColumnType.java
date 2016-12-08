
package org.javaosc.galaxy.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.galaxy.jdbc.core.ResultType;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class SingleColumnType<T> implements ResultType<T> {

    
    private final int columnIndex;

    
    private final String columnName;

    
    public SingleColumnType() {
        this(1, null);
    }

    
    public SingleColumnType(int columnIndex) {
        this(columnIndex, null);
    }

    
    public SingleColumnType(String columnName) {
        this(1, columnName);
    }

    
    private SingleColumnType(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            if (this.columnName == null) {
                return (T) rs.getObject(this.columnIndex);
            }
            return (T) rs.getObject(this.columnName);
        }
        return null;
    }
}
