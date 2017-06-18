
package org.javaosc.galaxy.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.galaxy.jdbc.core.ResultType;
import org.javaosc.galaxy.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MapType implements ResultType<Map<String, Object>> {

    
    private final RowConvert convert;

    
    public MapType() {
        this(ArrayType.ROW_PROCESSOR);
    }

    
    public MapType(RowConvert convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
