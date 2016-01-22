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
import org.javaosc.framework.constant.Constant.HttpType;
import org.javaosc.framework.web.util.StringUtil;
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
	private static long cacheValue;

	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = StringUtil.getStringValue(filterConfig.getInitParameter(Constant.SET_ENCODING_KEY), null);
		forceEnabled = StringUtil.getBooleanValue(filterConfig.getInitParameter(Constant.SET_ENFORC_ENCODING_KEY));
		getEnabled = StringUtil.getBooleanValue(filterConfig.getInitParameter(Constant.SET_HTTPGET_ENCODING_KEY));
		cacheValue = StringUtil.getLongValue(filterConfig.getInitParameter(Constant.SET_STATIC_CACHE_KEY),0);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		if (encoding!=null) {
			req = new RequestWrapper(req, encoding, (getEnabled && HttpType.GET.toString().equalsIgnoreCase(req.getMethod())));
			resp = forceEnabled == true ? new ResponseWrapper(resp, encoding, cacheValue) : new ResponseWrapper(resp, null, cacheValue);
		}
		filterChain.doFilter(req, resp);
	}
	
	public void destroy() {}

}
