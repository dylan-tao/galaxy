package org.javaosc.galaxy.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.assist.ClassHandler;
import org.javaosc.galaxy.assist.MethodParamHandler;
import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.context.ConfigHandler;
import org.javaosc.galaxy.context.ScanAnnotation;
import org.javaosc.galaxy.convert.ConvertFactory;
import org.javaosc.galaxy.util.PathUtil;
import org.javaosc.galaxy.util.StringUtil;
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
	private static boolean enableTemplate;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.init();
	}
	
	public void init(){
		prefix = ConfigHandler.getViewPrefix();
		suffix = ConfigHandler.getViewSuffix();
		try {
			Class.forName("httl.web.WebEngine");
			enableTemplate = true;
			log.info("Enable httl template rendering page");
		} catch (ClassNotFoundException e) {
			enableTemplate = false;
			log.info("Enable default jsp rendering page");
		}
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
			log.error(Constant.GALAXY_EXCEPTION, e);
		}finally{ 
			ActionContext.clear();
		}
	}
	
	private void executeMethod(HttpServletRequest request){
		
		String requestPath = PathUtil.getContextPath(request);
		String requestView = ConfigHandler.getViewMap(requestPath);

		if (StringUtil.isNotBlank(requestView)) {
			new ActionHandler(requestView).rendering(prefix, suffix, enableTemplate);
		} else { 
			Map<String, Object> routeMap = RouteNodeRegistry.getRouteNode(requestPath);		
			Object action = routeMap.get(RouteNodeRegistry.ACTION);
			Object method = routeMap.get(RouteNodeRegistry.METHOD);
			
			if(action!=null && method!=null){
				
				if(action instanceof Class<?>){
					Class<?> cls  = (Class<?>)action;
					action = ScanAnnotation.setServiceField(cls, ClassHandler.newInstance(cls));	
				}
				
				Method m = (Method)method;
				String[] param = ConvertFactory.convert(String[].class, routeMap.get(RouteNodeRegistry.METHOD_PRM));
				
				Object result = null;
				try {
					result = m.invoke(action, MethodParamHandler.getParamValue(m,m.getParameterTypes(), param));
				} catch (Exception e) {
					log.error(Constant.GALAXY_EXCEPTION, e);
				} 
				Class<?> returnType = m.getReturnType();
				if(returnType.equals(String.class)){
					String returnPath = String.valueOf(result);
					if(StringUtil.isNotBlank(returnPath)){
						new ActionHandler(returnPath).rendering(prefix, suffix, enableTemplate);
					}
					return;
				}else if(returnType.equals(void.class)){
					return;
				}else{ 
					log.error("the return type [{}] of the method [{}] is not supported !",returnType.getName(),m.getName());
				}	
			}else{
				if(routeMap.get(RouteNodeRegistry.ERROR_CODE)!=null){
					log.warn("the request path [{}] can not be found !",requestPath);
					new ActionHandler().sendError(404, null);
				}
			}
		}
	}
	
}
