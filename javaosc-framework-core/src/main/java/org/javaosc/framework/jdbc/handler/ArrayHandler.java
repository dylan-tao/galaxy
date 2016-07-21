
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.framework.jdbc.core.BasicRowProcessor;
import org.javaosc.framework.jdbc.core.ResultSetHandler;
import org.javaosc.framework.jdbc.core.RowProcessor;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {

    
    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    
    private static final Object[] EMPTY_ARRAY = new Object[0];

    
    private final RowProcessor convert;

    
    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    
    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
