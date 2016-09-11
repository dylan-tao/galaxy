
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.ratel.jdbc.core.RowAssist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ArrayListHandler extends AbstractListHandler<Object[]> {

    
    private final RowAssist convert;

    
    public ArrayListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    
    public ArrayListHandler(RowAssist convert) {
        super();
        this.convert = convert;
    }


    
    @Override
    protected Object[] handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}
