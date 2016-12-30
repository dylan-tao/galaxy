package org.javaosc.galaxy.constant;

import org.javaosc.galaxy.constant.Constant.CodeType;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Configuration {
	
	static final String CONFIG_FILE_NAME = "galaxyConfig";
	static final String EXPEND_FILE_NAME = "expandConfig";
	static final String CONFIG_HEAD_COMMENT = "galaxy configuration setting";
	
	static final String CONSOLE_STARTED_STATUS = "galaxy.console.started.status";
	static final String CONSOLE_METHOD_MONITOR = "galaxy.console.method.monitor";
	

	static final String PREFIX_KEY = "galaxy.view.prefix";
	static final String SUFFIX_KEY = "galaxy.view.suffix";
	
	static final String CONTEXT_ENCODE_KEY = "galaxy.context.encode";
	static final String REQUEST_ENCODE_KEY = "galaxy.request.character.encode";
	static final String RESPONSE_ENCODE_KEY = "galaxy.response.character.encode";
	
	static final String SCANER_PACKAGE_KEY = "galaxy.scaner.annotation.package";
	static final String METHOD_KEYWORD_KEY = "galaxy.transaction.read.method.keyword";
	
	static final String VIEW_KEY = "galaxy.url.";
	
	static final String STARTWITH_DB = "db.";
	static final String STARTWITH_POOL = "pool.";
	static final String POOL_DATASOURCE = "pool.dataSource";

	static final String DEFAULT_PREFIX_VALUE = "/";
	static final String DEFAULT_SUFFIX_VALUE = ".jsp";
	static final String DEFAULT_ENCODING_VALUE = CodeType.UTF8.getValue();
	static final String DEFAULT_ENCODING_FLAG = "true";
	static final String CLASS_KEYWORD_VALUE = "action,controller";
	
}
