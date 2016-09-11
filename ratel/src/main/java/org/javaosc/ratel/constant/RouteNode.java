package org.javaosc.ratel.constant;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class RouteNode {
	
	private String paramName;
	
	private Map<String,RouteNode> children;
	
	private Object action;
	
	private Method method;
	
	private String[] param;
	
	public RouteNode() {}
	
	public RouteNode(String paramName) {
		this.paramName = paramName;
	}
	
	public void addChild(String key,RouteNode child) {
		if(children != null){
			if(children.containsKey(key)){
				return;
			}else{
				children.put(key, child);
			}
		}else{
			children = new HashMap<String, RouteNode>();
			children.put(key, child);
		}
	}
	
	public RouteNode getChild(String key) {
		return children == null ? null : children.get(key);
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Object getAction() {
		return action;
	}

	public void setAction(Object action) {
		this.action = action;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}
	
}
