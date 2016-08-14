package org.javaosc.framework.assist;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.CodeType;
import org.javaosc.framework.constant.Constant.HttpType;
import org.javaosc.framework.context.ConfigurationHandler;
import org.javaosc.framework.web.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class RequestParamHandler {
	
	private static final Logger log = LoggerFactory.getLogger(RequestParamHandler.class);

	public static HashMap<String, Object> getFormatData(HttpServletRequest request,HttpServletResponse response) {

		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		Map<String, String[]> paramMap = request.getParameterMap();
		String requestType = request.getMethod();
		
		if(paramMap!=null && paramMap.size()>0){
			if (ConfigurationHandler.getRequestEncode() && (HttpType.GET.toString().equalsIgnoreCase(requestType) || HttpType.DELETE.toString().equalsIgnoreCase(requestType))) { // get\delete
				for(Entry<String, String[]> entry:paramMap.entrySet()){
					String key = entry.getKey();
					String[] values = entry.getValue();
					if (values == null) {
						dataMap.put(key, null);
					} else {
						String[] encodeValues = new String[values.length];
						for (String value : values) {
							value = encodingParam(value);
						}
						dataMap.put(key, encodeValues);
					}
				}
			} else { // post\put\get not open encode support
				for(Entry<String, String[]> entry:paramMap.entrySet()){
					String key = entry.getKey();
					String[] values = entry.getValue();
					if (values == null) {
						dataMap.put(key, null);
					} else {
						dataMap.put(key, values);
					}
				}
			}
		}
		return dataMap;
	}
	
	public static void put(String key, String value){
		if(ConfigurationHandler.getRequestEncode()){
			value = encodingParam(value);
		}
		ActionContext.getContext().put(key, value);
	}

	private static String encodingParam(String value) {
		try {
			byte[] b = value.getBytes(CodeType.ISO88591.getValue());
			value = new String(b, ConfigurationHandler.getContextEncode());
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}
		return value;
	}
}
