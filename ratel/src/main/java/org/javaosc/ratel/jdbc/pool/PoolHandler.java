package org.javaosc.ratel.jdbc.pool;

import javax.sql.DataSource;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface PoolHandler {
	
	DataSource getDataSource() throws Exception;
}
