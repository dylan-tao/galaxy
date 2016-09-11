
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.javaosc.framework.jdbc.core.ResultSetAssist;
import org.javaosc.framework.jdbc.core.RowAssist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanHandler<T> implements ResultSetAssist<T> {

    
    private final Class<T> type;

    
    private final RowAssist convert;

    
    public BeanHandler(Class<T> type) {
        this(type, ArrayHandler.ROW_PROCESSOR);
    }

    
    public BeanHandler(Class<T> type, RowAssist convert) {
        this.type = type;
        this.convert = convert;
    }

    
    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}
