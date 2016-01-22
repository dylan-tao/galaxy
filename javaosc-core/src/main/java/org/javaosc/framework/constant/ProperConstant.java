package org.javaosc.framework.constant;

import org.javaosc.framework.constant.Constant.CodeConstant;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface ProperConstant {
	
	String CONFIG_FILE_NAME = "configFileName";
	String CONFIG_HEAD_COMMENT = "uufast configuration setting";

	String PREFIX_KEY = "uufast.view.prefix";
	String SUFFIX_KEY = "uufast.view.suffix";
	String DEFAULT_ENCODE_KEY = "uufast.default.encode";
	String SCANER_PACKAGE_KEY = "uufast.scaner.annotation.package";
	String CLASS_KEYWORD_KEY = "uufast.scaner.classname.keyword";
	String MAPPING_PRMLOAD_KEY = "uufast.request.prm.preload"; 
	String MAPPING_PROCESSING_TIME_KEY = "uufast.request.processing.time"; 
	String METHOD_KEYWORD_KEY = "uufast.transaction.method.keyword";
	
	String DRIVER_CLASS_NAME = "jdbc.driverClassName";
	String JDBC_URL = "jdbc.url";
	String JDBC_USER_NAME = "jdbc.username";
	String JDBC_PASSWORD = "jdbc.password";
	
	String STARTWITH_DB = "db.";
	String STARTWITH_POOL = "pool.";
	String POOL_DATASOURCE = "pool.dataSource";

	String DEFAULT_PREFIX_VALUE = "/";
	String DEFAULT_SUFFIX_VALUE = ".jsp";
	String DEFAULT_ENCODING_VALUE = CodeConstant.UTF8.getValue();
	boolean STATIC_CACHE_VALUE = true;
	String CLASS_KEYWORD_VALUE = "action,controller";
	boolean MAPPING_PRELOAD_VALUE = true;
	boolean MAPPING_PROCESSING_TIME_VALUE = false;

}
