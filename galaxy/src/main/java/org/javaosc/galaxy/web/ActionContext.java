package org.javaosc.galaxy.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.assist.RequestParamHandler;
import org.javaosc.galaxy.context.ConfigHandler;

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
	private Map<String, Object> dataMap;
	
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
		
		if(ConfigHandler.getRequestEncode()){
			request.setCharacterEncoding(ConfigHandler.getContextEncode());
		}
		if(ConfigHandler.getResponseEncode()){
			response.setCharacterEncoding(ConfigHandler.getContextEncode());
		}
		
		actionContext.setRequest(request);
		actionContext.setResponse(response);
		
		actionContext.setDataMap(request, response);
		
		localContext.set(actionContext);
	}
	
	protected static void clear(){
		localContext.remove();
	}
	
	protected static void destroy(){
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

	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	
	public void put(String key, Object value) {
		dataMap.put(key, value);
	}

	public void setDataMap(HttpServletRequest request,HttpServletResponse response) {
		this.dataMap = RequestParamHandler.getFormatData(request, response);
	}
	
}
