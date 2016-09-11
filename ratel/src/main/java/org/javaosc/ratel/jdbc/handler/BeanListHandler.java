
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.javaosc.ratel.jdbc.core.ResultSetAssist;
import org.javaosc.ratel.jdbc.core.RowAssist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanListHandler<T> implements ResultSetAssist<List<T>> {

    
    private final Class<T> type;

    
    private final RowAssist convert;

    
    public BeanListHandler(Class<T> type) {
        this(type, ArrayHandler.ROW_PROCESSOR);
    }

    
    public BeanListHandler(Class<T> type, RowAssist convert) {
        this.type = type;
        this.convert = convert;
    }

    
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }
}
