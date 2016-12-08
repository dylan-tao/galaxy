
package org.javaosc.galaxy.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.javaosc.galaxy.jdbc.core.ResultType;
import org.javaosc.galaxy.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanListType<T> implements ResultType<List<T>> {

    
    private final Class<T> type;

    
    private final RowConvert convert;

    
    public BeanListType(Class<T> type) {
        this(type, ArrayType.ROW_PROCESSOR);
    }

    
    public BeanListType(Class<T> type, RowConvert convert) {
        this.type = type;
        this.convert = convert;
    }

    
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }
}
