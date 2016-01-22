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

	private String encoding;
	
	private long cacheValue;

	public ResponseWrapper(HttpServletResponse response, String encoding, long cacheValue) {
		super(response);
		this.encoding = encoding;
		this.cacheValue = cacheValue;
		initProperty();
	}
	
	private void initProperty(){
		super.setCharacterEncoding(encoding);
		if(cacheValue > 0){
			long nowTime = System.currentTimeMillis();
			super.setDateHeader("Last-Modified",nowTime);
			super.setDateHeader("Expires", nowTime + cacheValue);
			super.setHeader("Cache-Control", "public");
			super.setHeader("Pragma", "Pragma"); 
		}else if(cacheValue < 0){
			super.setDateHeader("Expires", 0);   
			super.setHeader("Cache-Control","no-store, no-cache, must-revalidation");
			super.addHeader("Cache-Control", "post-check=0, pre-check=0");
			super.setHeader("Pragma", "no-cache");   
		}
	} 

}
