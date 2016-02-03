package org.javaosc.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public static ActionContext getContext() {
		ActionContext context = localContext.get();
		if (context == null) {
			context = new ActionContext();
			localContext.set(context);
		}
		return context;
	}
	
	protected static void setContext(HttpServletRequest request,HttpServletResponse response){
		ActionContext actionContext = ActionContext.getContext();
		actionContext.setRequest(request);
		actionContext.setResponse(response);
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

}
