package org.javaosc.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ContextFilter implements Filter {

	private static String encoding;
	private static boolean forceEnabled;
	private static boolean getEnabled;

	public void init(FilterConfig config) throws ServletException {
		encoding = String.valueOf(config.getInitParameter(Constant.SET_ENCODING_KEY));
		forceEnabled = Boolean.valueOf(config.getInitParameter(Constant.SET_ENFORC_ENCODING_KEY));
		getEnabled = Boolean.valueOf(config.getInitParameter(Constant.SET_HTTPGET_ENCODING_KEY));
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		if (encoding!=null) {
			req = new RequestWrapper(req, getEnabled ,encoding);
			resp = forceEnabled?new ResponseWrapper(resp, encoding):resp;
		}
		filterChain.doFilter(req, resp);
	}
	
	public void destroy() {}

}
