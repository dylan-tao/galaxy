
package org.javaosc.ratel.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.ratel.jdbc.core.RowConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MapListType extends AbstractListType<Map<String, Object>> {

    
    private final RowConvert convert;

    
    public MapListType() {
        this(ArrayType.ROW_PROCESSOR);
    }

    
    public MapListType(RowConvert convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
