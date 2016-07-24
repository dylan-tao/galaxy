package org.javaosc.framework.web.filter;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.CodeType;
import org.javaosc.framework.constant.Constant.HttpType;
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
public class RequestWrapper extends HttpServletRequestWrapper {
	
	private static final Logger log = LoggerFactory.getLogger(RequestWrapper.class);

	private Map<String, String[]> requestData = new HashMap<String, String[]>();
	
	private boolean getEnabled;

	public RequestWrapper(HttpServletRequest request, boolean getEnabled, String encoding) throws UnsupportedEncodingException {
		super(request);
		super.setCharacterEncoding(encoding);
		super.setAttribute(Constant.SET_ENCODING_KEY, encoding);
		this.getEnabled = getEnabled;
		initRequestParam();
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.requestData;
	}

	@Override
	public String getParameter(String name) {
		return String.valueOf(this.requestData.get(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.requestData.get(name);
	}
	
	private void initRequestParam() {
		Enumeration<?> e = super.getParameterNames();
		String requestType = super.getMethod();
		
		if(getEnabled && (HttpType.GET.toString().equalsIgnoreCase(requestType) || HttpType.DELETE.toString().equalsIgnoreCase(requestType))){ //get\delete 
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String[] values = super.getParameterValues(key);
				if (values == null) {
					requestData.put(key, null);
				} else {
					String[] newValues = new String[values.length];
					for (String newValue:newValues) {
						newValue = encodingParam(newValue);
					}
					requestData.put(key, newValues);	
				}
			}
		}else{ // post\put\get not open encode support
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String[] values = super.getParameterValues(key);
				if (values == null) {
					requestData.put(key, null);
				} else {
					requestData.put(key, values);
				}
			}
		}
	}
	
	private String encodingParam(String value) {
		try {
			byte[] b = value.getBytes(CodeType.ISO88591.getValue());
			value = new String(b, this.getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}
		return value;
	}

}
