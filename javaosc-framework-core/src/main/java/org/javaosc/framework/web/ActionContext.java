package org.javaosc.framework.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.assist.RequestParamHandler;
import org.javaosc.framework.context.ConfigurationHandler;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public final class ActionContext {
	
	private static ThreadLocal<ActionContext> localContext = new ThreadLocal<ActionContext>();
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, String[]> dataMap;
	
	public static ActionContext getContext() {
		ActionContext context = localContext.get();
		if (context == null) {
			context = new ActionContext();
			localContext.set(context);
		}
		return context;
	}
	
	protected static void setContext(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		
		ActionContext actionContext = ActionContext.getContext();
		
		if(ConfigurationHandler.getRequestEncode()){
			request.setCharacterEncoding(ConfigurationHandler.getContextEncode());
		}
		if(ConfigurationHandler.getResponseEncode()){
			response.setCharacterEncoding(ConfigurationHandler.getContextEncode());
		}
		
		actionContext.setRequest(request);
		actionContext.setResponse(response);
		
		actionContext.setDataMap(request, response);
		
		localContext.set(actionContext);
	}
	
	public static void clear(){
		localContext.remove();
	}
	
	public static void destroy(){
		clear();
		localContext = null;
	}
	
	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Map<String, String[]> getDataMap() {
		return dataMap;
	}
	
	public void put(String key, String... value) {
		dataMap.put(key, value);
	}

	public void setDataMap(HttpServletRequest request,HttpServletResponse response) {
		this.dataMap = RequestParamHandler.getFormatData(request, response);
	}
	
}
