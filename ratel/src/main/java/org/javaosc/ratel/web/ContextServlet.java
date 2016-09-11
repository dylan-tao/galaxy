package org.javaosc.ratel.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.ratel.assist.MethodParamHandler;
import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.context.ConfigHandler;
import org.javaosc.ratel.convert.ConvertFactory;
import org.javaosc.ratel.util.PathUtil;
import org.javaosc.ratel.util.StringUtil;
import org.javaosc.ratel.web.assist.ActionHandler;
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
		prefix = ConfigHandler.getViewPrefix();
		suffix = ConfigHandler.getViewSuffix();
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
		String requestView = ConfigHandler.getViewMap(requestPath);

		if (StringUtil.isNotBlank(requestView)) {
			new ActionHandler(requestView).redirectOrForward(prefix, suffix);
		} else { 
			Map<String, Object> routeMap = RouteNodeRegistry.getRouteNode(requestPath);		
			Object action = routeMap.get(RouteNodeRegistry.ACTION);
			Object method = routeMap.get(RouteNodeRegistry.METHOD);
			
			if(action!=null && method!=null){
//				if(action.){
//					
//				}
				
				Method m = (Method)method;
				String[] param = ConvertFactory.convert(String[].class, routeMap.get(RouteNodeRegistry.METHOD_PRM));
				
				Object result = null;
				try {
					result = m.invoke(action, MethodParamHandler.getParamValue(m,m.getParameterTypes(), param));
				} catch (Exception e) {
					log.error(Constant.JAVAOSC_EXCEPTION, e);
				} 
				Class<?> returnType = m.getReturnType();
				if(returnType.equals(String.class)){
					String returnPath = String.valueOf(result);
					if(StringUtil.isNotBlank(returnPath)){
						new ActionHandler(returnPath).redirectOrForward(prefix, suffix);
					}
					return;
				}else if(returnType.equals(void.class)){
					return;
				}else{ 
					log.error("the return type [{}] of the method [{}] is not supported !",returnType.getName(),m.getName());
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
