
package org.javaosc.framework.jdbc.core;


import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class GenerousBeanProcessor extends BeanProcessor {

    
    public GenerousBeanProcessor() {
        super();
    }

    @Override
    protected int[] mapColumnsToProperties(final ResultSetMetaData rsmd,
            final PropertyDescriptor[] props) throws SQLException {

        final int cols = rsmd.getColumnCount();
        final int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);

            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }

            final String generousColumnName = columnName.replace("_", "");

            for (int i = 0; i < props.length; i++) {
                final String propName = props[i].getName();

                // see if either the column name, or the generous one matches
                if (columnName.equalsIgnoreCase(propName) ||
                        generousColumnName.equalsIgnoreCase(propName)) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

}
