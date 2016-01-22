package org.javaosc.framework.web.filter;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.CodeConstant;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private Map<String, Object> requestData = new HashMap<String, Object>();

	private String encoding;
	
	private boolean isHttpGet;

	public RequestWrapper(HttpServletRequest request, String encoding,boolean isHttpGet) {
		super(request);
		this.encoding = encoding;
		this.isHttpGet = isHttpGet;
		initProperty();
		initDataMap();
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return this.requestData;
	}

	@Override
	public String getParameter(String name) {
		Object obj = this.requestData.get(name);
		return obj != null?obj.toString():null;
	}

	@Override
	public String[] getParameterValues(String name) {
		Object obj = this.requestData.get(name);
		return obj!=null?(String[])obj:null;
	}
	
	private void initProperty(){
		try {
			super.setCharacterEncoding(encoding);
			super.setAttribute(Constant.SET_ENCODING_KEY, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	} 
	
	private void initDataMap() {
		Enumeration<?> e = super.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String[] values = super.getParameterValues(key);
			if (values == null) {
				requestData.put(key, Constant.EMPTY);
			} else if (values.length == 1) {
				String value = isHttpGet == true ? encodingPrm(values[0]) : values[0];
				requestData.put(key, value);
			} else {
				if(isHttpGet == true){
					String[] newValues = new String[values.length];
					for (int u = 0; u < values.length; u++) {
						newValues[u] = encodingPrm(values[u]);
					}
					requestData.put(key, newValues);
				}else{
					requestData.put(key, values);
				}
			}
		}
	}
	
	private String encodingPrm(String value) {
		try {
			byte[] b = value.getBytes(CodeConstant.ISO88591.getValue());
			value = new String(b, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}

}
