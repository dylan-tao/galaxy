
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ColumnListHandler<T> extends AbstractListHandler<T> {

    
    private final int columnIndex;

    
    private final String columnName;

    
    public ColumnListHandler() {
        this(1, null);
    }

    
    public ColumnListHandler(int columnIndex) {
        this(columnIndex, null);
    }

    
    public ColumnListHandler(String columnName) {
        this(1, columnName);
    }

    
    private ColumnListHandler(int columnIndex, String columnName) {
        super();
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    @SuppressWarnings("unchecked")
    @Override
    protected T handleRow(ResultSet rs) throws SQLException {
        if (this.columnName == null) {
            return (T) rs.getObject(this.columnIndex);
        }
        return (T) rs.getObject(this.columnName);
   }

}
