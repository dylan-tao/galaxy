package org.javaosc.galaxy.web;

import httl.web.WebEngine;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ActionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ActionHandler.class);
	
	private String path;
	
	public ActionHandler() {}
	
	public ActionHandler(String path) {
		this.path = path;
	}
	
	public void rendering(String prefix, String suffix, boolean enableTemplate){
		if(path.startsWith(Constant.COLON) || path.startsWith(Constant.COLON_EXTEND)){
			this.redirect(path.substring(1));
		}else{
			String viewPath = prefix + Constant.LINE + path + suffix;
			if(enableTemplate){ //httl
				this.httlView(viewPath);
			}else{
				this.forward(viewPath);
			}
		}	
	}
	
	public void sendError(int code, String message){
		try {
			ActionContext.getContext().getResponse().sendError(code, message);
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
	}
	
	protected void forward(String viewPath) {
		RequestDispatcher dispatcher = ActionContext.getContext().getRequest().getRequestDispatcher(viewPath);
		try {
			dispatcher.forward(ActionContext.getContext().getRequest(), ActionContext.getContext().getResponse());
		} catch (ServletException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
	}

	protected void redirect(String servicePath) {
		 try {
			 ActionContext.getContext().getResponse().sendRedirect(servicePath);
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
	}
	
	protected void httlView(String viewPath) {
		HttpServletRequest request = ActionContext.getContext().getRequest();
		HttpServletResponse response = ActionContext.getContext().getResponse();
		try {
			WebEngine.setRequestAndResponse(request, response);
			WebEngine.getEngine().getTemplate(viewPath, request.getLocale()).render(response);
		} catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}	 
	}
	
}
