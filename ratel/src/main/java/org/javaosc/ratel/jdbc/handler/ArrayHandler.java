
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.ratel.jdbc.core.ResultSetAssist;
import org.javaosc.ratel.jdbc.core.RowAssist;
import org.javaosc.ratel.jdbc.core.RowAssistImpl;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ArrayHandler implements ResultSetAssist<Object[]> {

    
    static final RowAssist ROW_PROCESSOR = new RowAssistImpl();

    
    private static final Object[] EMPTY_ARRAY = new Object[0];

    
    private final RowAssist convert;

    
    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    
    public ArrayHandler(RowAssist convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
