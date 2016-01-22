package org.javaosc.framework.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.web.util.StringUtil;
import org.javaosc.framework.web.util.StringUtil.PatternValue;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ContextResult {
	
	private String path;
	
	private Map<String, Object> returnData;
	
	private String key;
	
	private Object value;
	
	private boolean isOne;
	
	private HttpServletRequest request = ActionContext.getContext().getRequest();
	
	private HttpServletResponse response = ActionContext.getContext().getResponse();
	
	
	public ContextResult(String path, Map<String, Object> returnData) {
		this.path = path;
		this.returnData = returnData;
		this.isOne = false;
	}
	
	public ContextResult(String path, String key, Object value) {
		this.path = path;
		this.key = key;
		this.value = value;
		this.isOne = true;
	}
	
	protected void redirectOrForward(String prefix, String suffix){
		if(StringUtil.isNotBlank(path)){
			path = StringUtil.clearSpace(path, PatternValue.ALL);
			if(path.startsWith(Constant.COLON) || path.startsWith(Constant.COLON_EXTEND)){
				path = path.substring(1) + ActionHandler.redirectResult(isOne, key, value, returnData);
				this.redirect();
			}else{
				forwardResult();
				path = prefix + Constant.LINE + path + suffix;
				this.forward();
			}
		}else{
			this.sendError(404);
		}
	}
	
	private void forwardResult(){
		if(isOne){
			if(StringUtil.isNotBlank(key)){
			   request.setAttribute(key, value);
			}
		}else{
			if(returnData != null){
				Iterator<Entry<String, Object>> iterator = returnData.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, Object> entry = iterator.next();
					request.setAttribute(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	private void forward() {
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void redirect() {
		 try {
			 response.sendRedirect(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendError(int code){
		try {
			response.sendError(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
