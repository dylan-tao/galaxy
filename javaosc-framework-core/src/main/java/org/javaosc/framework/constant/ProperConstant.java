package org.javaosc.framework.constant;

import org.javaosc.framework.constant.Constant.CodeType;

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
	String DEFAULT_ENCODING_VALUE = CodeType.UTF8.getValue();
	String CLASS_KEYWORD_VALUE = "action,controller";
	
}
