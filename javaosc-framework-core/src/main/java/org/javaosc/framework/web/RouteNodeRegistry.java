package org.javaosc.framework.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javaosc.framework.assist.MethodPrmHandler;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.RouteNode;
import org.javaosc.framework.util.StringUtil;
import org.javaosc.framework.util.StringUtil.PatternValue;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public abstract class RouteNodeRegistry {
	
	private static RouteNode root = new RouteNode();
	
	private static final Pattern paramPattern = Pattern.compile("\\{([a-zA-Z_]+[0-9]*)\\}");
	
	private static final String URI_PARAM = "_$_URI_PARAM_$___";
	
	protected static final String ACTION_CLASS = "_$_ACTION_CLASS_$___";
	
	protected static final String METHOD = "_$_METHOD_$___";
	
	protected static final String METHOD_PRM = "_$_METHOD_PRM_$___";
	
	protected static final String ERROR_CODE = "_$_ERROR_CODE_$___";
	
	
	public static void registerRouteNode(String uriPattern, Class<?> cls, Method method){
		if(StringUtil.isNotBlank(uriPattern)){
			uriPattern = StringUtil.clearSpace(uriPattern, PatternValue.ALL);
			String[] routePath = uriPattern.split(Constant.LINE);
			RouteNode current = root;
			RouteNode child = null;
			int uriLength = routePath.length;
			
			for (int i = 0;i<uriLength;i++){
				String urlSplitStr = routePath[i];
				if (Constant.EMPTY.equals(urlSplitStr)) continue;
				child = current.getChild(urlSplitStr);
				if (child == null){
					if(urlSplitStr.startsWith("{")){
						Matcher matcher = paramPattern.matcher(urlSplitStr);
						if (matcher.matches()){
							if((child = current.getChild(URI_PARAM)) == null){
								child = new RouteNode(matcher.group(1));
								if(uriLength-i == 1){ 
									child.setCls(cls);
									child.setMethod(method);
									String[] methodPrm = MethodPrmHandler.getParamName(method);
									child.setParam(methodPrm);	
								}
								current.addChild(URI_PARAM, child);
							}
						}
					}else{
						child = new RouteNode();
						if(uriLength-i == 1){ 
							child.setCls(cls);
							child.setMethod(method);
							String[] methodPrm = MethodPrmHandler.getParamName(method);
							child.setParam(methodPrm);	
						}
						current.addChild(urlSplitStr, child);
					}
				}
				current = child;
				child = null;
			}
		}
	}
	
	protected static Map<String, Object> getRouteNode(String uri){
		Map<String, Object> params = new HashMap<String, Object>();
		String[] routePath = uri.split(Constant.LINE);
		RouteNode current = root;
		RouteNode child = null;
		int uriLength = routePath.length;
		
		for (int i = 0;i<uriLength;i++){
			String urlSplitStr = routePath[i];
			if (Constant.EMPTY.equals(urlSplitStr)) continue;
			child = current.getChild(urlSplitStr);
			if (child == null){
				child = current.getChild(URI_PARAM);
				if (child != null){
					params.put(child.getParamName(), urlSplitStr);
				}else{
					params.put(ERROR_CODE, 0);
					break;
				}
			}
			
			if(uriLength-i == 1){ 
				params.put(ACTION_CLASS, child.getCls());
				params.put(METHOD, child.getMethod());
				params.put(METHOD_PRM, child.getParam());
			}
			current = child;
			child = null;
		}
		return params;
	}
	
	public static void clear(){
		root = null;
	}
	
}
