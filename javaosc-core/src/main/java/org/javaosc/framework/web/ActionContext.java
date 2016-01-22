package org.javaosc.framework.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public final class ActionContext {
	
	private static final Logger log = LoggerFactory.getLogger(ActionContext.class);

	private static ThreadLocal<ActionContext> localContext = new ThreadLocal<ActionContext>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext application;

	public static ActionContext getContext() {
		ActionContext context = localContext.get();
		if (context == null) {
			context = new ActionContext();
			localContext.set(context);
		}
		return context;
	}
	
	protected static void setContext(HttpServletRequest request,HttpServletResponse response,ServletContext application){
		ActionContext actionContext = ActionContext.getContext();
		actionContext.setRequest(request);
		actionContext.setResponse(response);
		actionContext.setApplication(application);
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

	public ServletContext getApplication() {
		return application;
	}

	public void setApplication(ServletContext application) {
		this.application = application;
	}

}
