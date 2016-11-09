package org.javaosc.ratel.constant;

import org.javaosc.ratel.constant.Constant.CodeType;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Configuration {
	
	static final String CONFIG_FILE_NAME = "ratelConfig";
	static final String EXPEND_FILE_NAME = "expandConfig";
	static final String CONFIG_HEAD_COMMENT = "ratel configuration setting";

	static final String PREFIX_KEY = "ratel.view.prefix";
	static final String SUFFIX_KEY = "ratel.view.suffix";
	
	static final String CONTEXT_ENCODE_KEY = "ratel.context.encode";
	static final String REQUEST_ENCODE_KEY = "ratel.request.character.encode";
	static final String RESPONSE_ENCODE_KEY = "ratel.response.character.encode";
	
	static final String SCANER_PACKAGE_KEY = "ratel.scaner.annotation.package";
	static final String METHOD_KEYWORD_KEY = "ratel.read.transaction.method.keyword";
	
	static final String VIEW_KEY = "ratel.url.";
	
	static final String STARTWITH_DB = "db.";
	static final String STARTWITH_POOL = "pool.";
	static final String POOL_DATASOURCE = "pool.dataSource";

	static final String DEFAULT_PREFIX_VALUE = "/";
	static final String DEFAULT_SUFFIX_VALUE = ".jsp";
	static final String DEFAULT_ENCODING_VALUE = CodeType.UTF8.getValue();
	static final String DEFAULT_ENCODING_FLAG = "true";
	static final String CLASS_KEYWORD_VALUE = "action,controller";
	
}
