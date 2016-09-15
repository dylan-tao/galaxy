
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.ratel.jdbc.core.ResultType;
import org.javaosc.ratel.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanHandler<T> implements ResultType<T> {

    
    private final Class<T> type;

    
    private final RowConvert convert;

    
    public BeanHandler(Class<T> type) {
        this(type, ArrayHandler.ROW_PROCESSOR);
    }

    
    public BeanHandler(Class<T> type, RowConvert convert) {
        this.type = type;
        this.convert = convert;
    }

    
    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}
