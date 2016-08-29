package org.javaosc.framework.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.assist.MethodParamHandler;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.context.ConfigurationHandler;
import org.javaosc.framework.util.PathUtil;
import org.javaosc.framework.util.StringUtil;
import org.javaosc.framework.web.assist.ActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ContextServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(ContextServlet.class);
	
	private static String prefix;
	private static String suffix;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.init();
	}
	
	public void init(){
		prefix = ConfigurationHandler.getViewPrefix();
		suffix = ConfigurationHandler.getViewSuffix();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}
	
	private void doDispatcher(HttpServletRequest request, HttpServletResponse response) {
		try {
			ActionContext.setContext(request, response);
			executeMethod(request);
		} catch (Exception e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}finally{ 
			ActionContext.clear();
		}
	}
	
	private void executeMethod(HttpServletRequest request){
		
		String requestPath = PathUtil.getContextPath(request);
		String requestView = ConfigurationHandler.getViewMap(requestPath);

		if (StringUtil.isNotBlank(requestView)) {
			new ActionHandler(requestView).redirectOrForward(prefix, suffix);
		} else { 
			Map<String, Object> routeMap = RouteNodeRegistry.getRouteNode(requestPath);		
			Object action = routeMap.get(RouteNodeRegistry.ACTION);
			Object m = routeMap.get(RouteNodeRegistry.METHOD);
			
			if(action!=null && m!=null){
				
				Method method = (Method)m;
				Object methodPrm = routeMap.get(RouteNodeRegistry.METHOD_PRM);
				String[] param = methodPrm==null?null:(String[])methodPrm;
				
				Object result = null;
				try {
					result = method.invoke(action, MethodParamHandler.getParamValue(method,method.getParameterTypes(), param));
				} catch (Exception e) {
					log.error(Constant.JAVAOSC_EXCEPTION, e);
				} 
				Class<?> returnType = method.getReturnType();
				if(returnType.equals(String.class)){
					String returnPath = String.valueOf(result);
					if(StringUtil.isNotBlank(returnPath)){
						new ActionHandler(returnPath).redirectOrForward(prefix, suffix);
					}
					return;
				}else if(returnType.equals(void.class)){
					return;
				}else{ 
					log.error("the return type [{}] of the method [{}] is not supported !",returnType.getName(),method.getName());
				}	
			}else{
				if(routeMap.get(RouteNodeRegistry.ERROR_CODE)!=null){
					log.debug("the request path [{}] can not be found !",requestPath);
					try {
						ActionContext.getContext().getResponse().sendError(404);
					} catch (IOException e) {
						log.error(Constant.JAVAOSC_EXCEPTION, e);
					}
				}
			}
		}
	}
	
}
