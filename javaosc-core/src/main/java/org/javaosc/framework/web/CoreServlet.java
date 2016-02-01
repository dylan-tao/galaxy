package org.javaosc.framework.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.Configuration;
import org.javaosc.framework.web.assist.ParamValueAssist;
import org.javaosc.framework.web.util.PathUtil;
import org.javaosc.framework.web.util.StringUtil;
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
	private static boolean processTime;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		this.init();
	}
	
	public void init(){
		prefix = Configuration.getValue(ProperConstant.PREFIX_KEY, ProperConstant.DEFAULT_PREFIX_VALUE);
		suffix = Configuration.getValue(ProperConstant.SUFFIX_KEY, ProperConstant.DEFAULT_SUFFIX_VALUE);
		processTime = Configuration.getValue(ProperConstant.MAPPING_PROCESSING_TIME_KEY, ProperConstant.MAPPING_PROCESSING_TIME_VALUE);
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
			if(processTime){
				long startTime = System.currentTimeMillis();
				executeMethod(request);
				startTime = System.currentTimeMillis() - startTime;
				log.info("this request path [" + PathUtil.getContextPath(request) + "] in " + startTime + " ms");
			}else{
				executeMethod(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			ActionContext.clear();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void executeMethod(HttpServletRequest request){
		
		String requestPath = PathUtil.getContextPath(request);
		String requestValue = Configuration.getValue(requestPath);

		if (StringUtil.isNotBlank(requestValue)) {
			ContextResult contextResult = new ContextResult(requestValue, null, null);
			contextResult.redirectOrForward(prefix, suffix);
		} else { 
			Map<String, Object> routeMap = RouteNodeRegistry.getRouteNode(requestPath);		
			Object actionObj = routeMap.get(RouteNodeRegistry.ACTION_CLASS);
			Object methodObj = routeMap.get(RouteNodeRegistry.METHOD);
			Object methodPrmObj = routeMap.get(RouteNodeRegistry.METHOD_PRM);
			
			Class<?> actionCls = actionObj==null?null:(Class<?>)actionObj;
			Method method = methodObj==null?null:(Method)methodObj;
			List<String> methodPrm = methodPrmObj==null?null:(List<String>)methodPrmObj;
			
			if(actionCls!=null && methodObj!=null){
				
				if(routeMap.size() > 0){
					routeMap.remove(RouteNodeRegistry.ACTION_CLASS);
					routeMap.remove(RouteNodeRegistry.METHOD);
					routeMap.remove(RouteNodeRegistry.METHOD_PRM);
					ActionContext.getContext().getRequest().getParameterMap().putAll(routeMap);
				}
				
				Object action = BeanFactory.getBean(actionCls, false);
				
				if (method!=null) {
					Object returnObj = null;
					try {
						if(processTime){
							log.info("execute the method [" + actionCls.getName() + Constant.DOT + method.getName() + "()] ");
						}
						returnObj = method.invoke(action, ParamValueAssist.getPrmValue(method,method.getParameterTypes(), methodPrm));
					} catch (IllegalArgumentException e) {
						log.error("[errorCode:1109] please check this method ["+method.getName()+"] of parameter is correct and see the following Caused by: !",e);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					Class<?> returnType = method.getReturnType();
					if(returnType == ContextResult.class){
						ContextResult contextResult = (ContextResult)returnObj;
						contextResult.redirectOrForward(prefix, suffix);
					}else if(returnType == String.class){
						String returnPath = returnObj == null ? null : returnObj.toString();
						ContextResult contextResult = new ContextResult(returnPath, null, null);
						contextResult.redirectOrForward(prefix, suffix);
						return;
					}else if(returnType == void.class){
						return;
					}else{ 
						log.error("[errorCode:1110] the return type ["+returnType.getName()+"] of the method [" + method.getName() + "] is not supported !");
					}
				}else{ 
					log.error("[errorCode:1111] this request path ["+requestPath+"] can't find pointing to the method, maybe not bind !");
				}	
			}else{
				if(routeMap.get(RouteNodeRegistry.ERROR_CODE)!=null){
					log.debug("[errorCode:1112] this request path ["+requestPath+"] can not find !");
					try {
						ActionContext.getContext().getResponse().sendError(404);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
