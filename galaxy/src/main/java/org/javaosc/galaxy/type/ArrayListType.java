
package org.javaosc.galaxy.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.galaxy.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ArrayListType extends AbstractListType<Object[]> {

    
    private final RowConvert convert;

    
    public ArrayListType() {
        this(ArrayType.ROW_PROCESSOR);
    }

    
    public ArrayListType(RowConvert convert) {
        super();
        this.convert = convert;
    }


    
    @Override
    protected Object[] handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}
