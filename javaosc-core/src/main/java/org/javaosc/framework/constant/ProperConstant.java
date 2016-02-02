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
	String CONFIG_HEAD_COMMENT = "javaosc configuration setting";

	String PREFIX_KEY = "javaosc.view.prefix";
	String SUFFIX_KEY = "javaosc.view.suffix";
	String DEFAULT_ENCODE_KEY = "javaosc.default.encode";
	String SCANER_PACKAGE_KEY = "javaosc.scaner.annotation.package";
	String CLASS_KEYWORD_KEY = "javaosc.scaner.classname.keyword";
	String MAPPING_PRMLOAD_KEY = "javaosc.request.prm.preload"; 
	String MAPPING_PROCESSING_TIME_KEY = "javaosc.request.processing.time"; 
	String METHOD_KEYWORD_KEY = "javaosc.transaction.method.keyword";
	
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
	String CLASS_KEYWORD_VALUE = "action,controller";
	boolean MAPPING_PRELOAD_VALUE = true;
	boolean MAPPING_PROCESSING_TIME_VALUE = false;

}
