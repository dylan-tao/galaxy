
package org.javaosc.framework.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.framework.jdbc.core.ResultSetHandler;
import org.javaosc.framework.jdbc.core.RowProcessor;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    
    private final RowProcessor convert;

    
    public MapHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    
    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
