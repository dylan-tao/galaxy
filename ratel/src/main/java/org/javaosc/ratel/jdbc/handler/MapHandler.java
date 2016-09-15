
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.ratel.jdbc.core.ResultType;
import org.javaosc.ratel.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MapHandler implements ResultType<Map<String, Object>> {

    
    private final RowConvert convert;

    
    public MapHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    
    public MapHandler(RowConvert convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
