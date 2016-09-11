
package org.javaosc.framework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface ResultSetAssist<T> {

    
    T handle(ResultSet rs) throws SQLException;

}