
package org.javaosc.framework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface RowProcessor {

    
    Object[] toArray(ResultSet rs) throws SQLException;

    
    <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

    
    <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;

    
    Map<String, Object> toMap(ResultSet rs) throws SQLException;

}
