
package org.javaosc.galaxy.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.galaxy.core.ResultType;
import org.javaosc.galaxy.core.RowConvert;
import org.javaosc.galaxy.core.RowConvertHandler;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ArrayType implements ResultType<Object[]> {

    
    static final RowConvert ROW_PROCESSOR = new RowConvertHandler();

    
    private static final Object[] EMPTY_ARRAY = new Object[0];

    
    private final RowConvert convert;

    
    public ArrayType() {
        this(ROW_PROCESSOR);
    }

    
    public ArrayType(RowConvert convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
