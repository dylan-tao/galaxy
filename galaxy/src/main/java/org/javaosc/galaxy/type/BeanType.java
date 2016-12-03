
package org.javaosc.galaxy.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.galaxy.core.ResultType;
import org.javaosc.galaxy.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanType<T> implements ResultType<T> {

    
    private final Class<T> type;

    
    private final RowConvert convert;

    
    public BeanType(Class<T> type) {
        this(type, ArrayType.ROW_PROCESSOR);
    }

    
    public BeanType(Class<T> type, RowConvert convert) {
        this.type = type;
        this.convert = convert;
    }

    
    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}
