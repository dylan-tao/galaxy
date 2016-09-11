package org.javaosc.ratel.util;

import javax.servlet.http.HttpServletRequest;

import org.javaosc.ratel.constant.Constant;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class PathUtil {
	
	/**
	 * request url
	 * http://localhost:8080/ratel/list?pageNo=1(ratel is project's name)
	 * 
	 **/
	
	/**
	 * 获取项目根目录
	 * @return String /ratel
	 */
	public static String getContextRoot(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	/**
	 * 获取请求路径
	 * @return String /list
	 */
	public static String getContextPath(HttpServletRequest request) {
		return request.getServletPath();
	}
	
	/**
	 * 获取项目及请求路径
	 * @return String /ratel/list
	 */
	public static String getContextUri(HttpServletRequest request) {
		return request.getRequestURI();
	}
	
	/**
	 * 获取项目及请求路径
	 * @return String http://localhost:8080/ratel/list
	 */
	public static String getContextUrl(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}
	
	/**
	 * 获取class编译根目录
	 * @return String .../WEB-INF/classes/
	 */
	public static String getClassPath() {
		return PathUtil.class.getResource(Constant.LINE).toString().replace("file:/", Constant.EMPTY);
	}
	
	/**
	 * 获取get请求的参数
	 * @return String pageNo=1
	 */
	public static String getQueryString(HttpServletRequest request) {
		return request.getQueryString();
	}
	
}
