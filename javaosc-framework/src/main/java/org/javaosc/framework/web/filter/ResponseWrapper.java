package org.javaosc.framework.web.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
	
	public ResponseWrapper(HttpServletResponse response, String encoding) {
		super(response);
		super.setCharacterEncoding(encoding);
	}
	
}
