
package org.javaosc.ratel.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.javaosc.ratel.jdbc.core.RowAssist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

    
    private final RowAssist convert;

    
    public MapListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    
    public MapListHandler(RowAssist convert) {
        super();
        this.convert = convert;
    }

    
    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
