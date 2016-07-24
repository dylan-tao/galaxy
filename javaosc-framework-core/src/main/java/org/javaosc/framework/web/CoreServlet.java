package org.javaosc.framework.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.assist.MethodPrmHandler;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.Configuration;
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
public class CoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(CoreServlet.class);
	
	private static String prefix;
	private static String suffix;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.init();
	}
	
	public void init(){
		prefix = Configuration.getValue(ProperConstant.PREFIX_KEY, ProperConstant.DEFAULT_PREFIX_VALUE);
		suffix = Configuration.getValue(ProperConstant.SUFFIX_KEY, ProperConstant.DEFAULT_SUFFIX_VALUE);
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
		String requestValue = Configuration.getValue(requestPath);

		if (StringUtil.isNotBlank(requestValue)) {
			new ActionHandler(requestValue).redirectOrForward(prefix, suffix);
		} else { 
			Map<String, Object> routeMap = RouteNodeRegistry.getRouteNode(requestPath);		
			Object actionObj = routeMap.get(RouteNodeRegistry.ACTION_CLASS);
			Object methodObj = routeMap.get(RouteNodeRegistry.METHOD);
			Object methodPrmObj = routeMap.get(RouteNodeRegistry.METHOD_PRM);
			
			Class<?> actionCls = actionObj==null?null:(Class<?>)actionObj;
			Method method = methodObj==null?null:(Method)methodObj;
			String[] methodPrm = methodPrmObj==null?null:(String[])methodPrmObj;
			
			if(actionCls!=null && methodObj!=null){
				
				Object action = BeanFactory.getBean(actionCls, false);
				
				if (method!=null) {
					Object returnObj = null;
					try {
						returnObj = method.invoke(action, MethodPrmHandler.getParamValue(method,method.getParameterTypes(), methodPrm));
					} catch (IllegalArgumentException e) {
						log.error(Constant.JAVAOSC_EXCEPTION, e);
					} catch (IllegalAccessException e) {
						log.error(Constant.JAVAOSC_EXCEPTION, e);
					} catch (InvocationTargetException e) {
						log.error(Constant.JAVAOSC_EXCEPTION, e);
					}
					Class<?> returnType = method.getReturnType();
					if(returnType.equals(String.class)){
						String returnPath = String.valueOf(returnObj);
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
					log.error("this request path [{}] can't find pointing to the method, maybe not bind !", requestPath);
				}	
			}else{
				if(routeMap.get(RouteNodeRegistry.ERROR_CODE)!=null){
					log.debug("this request path [{}] can not find !",requestPath);
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
