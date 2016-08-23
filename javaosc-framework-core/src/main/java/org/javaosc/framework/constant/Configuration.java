package org.javaosc.framework.constant;

import org.javaosc.framework.constant.Constant.CodeType;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Configuration {
	
	static final String CONFIG_FILE_NAME = "javaoscConfig";
	static final String CONFIG_HEAD_COMMENT = "javaosc configuration setting";

	static final String PREFIX_KEY = "javaosc.view.prefix";
	static final String SUFFIX_KEY = "javaosc.view.suffix";
	
	static final String CONTEXT_ENCODE_KEY = "javaosc.context.encode";
	static final String REQUEST_ENCODE_KEY = "javaosc.request.character.encode";
	static final String RESPONSE_ENCODE_KEY = "javaosc.response.character.encode";
	
	static final String SCANER_PACKAGE_KEY = "javaosc.scaner.annotation.package";
	static final String METHOD_KEYWORD_KEY = "javaosc.transaction.method.keyword";
	
	static final String VIEW_KEY = "javaosc.url.";
	
	static final String STARTWITH_DB = "db.";
	static final String STARTWITH_POOL = "pool.";
	static final String POOL_DATASOURCE = "pool.dataSource";

	static final String DEFAULT_PREFIX_VALUE = "/";
	static final String DEFAULT_SUFFIX_VALUE = ".jsp";
	static final String DEFAULT_ENCODING_VALUE = CodeType.UTF8.getValue();
	static final String DEFAULT_ENCODING_FLAG = "true";
	static final String CLASS_KEYWORD_VALUE = "action,controller";
	
}
