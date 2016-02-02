package org.javaosc.framework.web.assist;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.web.ActionContext;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ActionHandler {
	
	private String path;
	
	public ActionHandler(String path) {
		this.path = path;
	}
	
	public void redirectOrForward(String prefix, String suffix){
		if(path.startsWith(Constant.COLON) || path.startsWith(Constant.COLON_EXTEND)){
			this.redirect(path.substring(1));
		}else{
			this.forward(new StringBuffer(prefix).append(Constant.LINE).append(path).append(suffix).toString());
		}	
	}
	
	private void forward(String viewPath) {
		RequestDispatcher dispatcher = ActionContext.getContext().getRequest().getRequestDispatcher(viewPath);
		try {
			dispatcher.forward(ActionContext.getContext().getRequest(), ActionContext.getContext().getResponse());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void redirect(String servicePath) {
		 try {
			 ActionContext.getContext().getResponse().sendRedirect(servicePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
