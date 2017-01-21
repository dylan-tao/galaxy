package org.javaosc.galaxy.assist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.CodeType;
import org.javaosc.galaxy.constant.Constant.HttpType;
import org.javaosc.galaxy.constant.Constant.ReqContentType;
import org.javaosc.galaxy.context.ConfigHandler;
import org.javaosc.galaxy.util.CodeUtil;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.javaosc.galaxy.util.JsonUtil;
import org.javaosc.galaxy.web.ActionContext;
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
		
		if (ConfigHandler.getRequestEncode() && (HttpType.GET.toString().equalsIgnoreCase(requestType) || HttpType.DELETE.toString().equalsIgnoreCase(requestType))) { // get\delete
			if(paramMap!=null && paramMap.size()>0){
				for(Entry<String, String[]> entry:paramMap.entrySet()){
					String key = entry.getKey();
					String[] values = entry.getValue();
					if (values == null) {
						dataMap.put(key, null);
					}else if(values.length==1){
						dataMap.put(key, encodingParam(values[0]));
					} else {
						String[] encodeValues = new String[values.length];
						for (int i=0;i<values.length;i++) {
							encodeValues[i] = encodingParam(values[i]);
						}
						dataMap.put(key, encodeValues);
					}
				}
			}
		} else { // post\put not open encode support
			String contentType = request.getContentType();
			System.out.println(contentType);
			if(contentType.startsWith(ReqContentType.WWW_FORM_URLENCODED.getValue())){//www-form-urlencoded
				for(Entry<String, String[]> entry:paramMap.entrySet()){
					String key = entry.getKey();
					String[] values = entry.getValue();
					if (values == null) {
						dataMap.put(key, null);
					}else if(values.length==1){
						dataMap.put(key, values[0]);
					} else {
						dataMap.put(key, values);
					}
				}
			}else if(contentType.startsWith(ReqContentType.MULTIPART_FORM_DATA.getValue())){ //multipart or form data
				
				
			}else{ //try format input stream or json data from body
				String bodyData = getBodyString(request);
				if(!GalaxyUtil.isEmpty(bodyData)){
					bodyData = CodeUtil.decodeURL(bodyData, CodeType.getCodeType(ConfigHandler.getContextEncode()));
					if(JsonUtil.check(bodyData)){ //json
						dataMap.put(Constant.JSON_PARAM, bodyData);
					}else{ //&
						dataMap.putAll(GalaxyUtil.getQueryArray(bodyData));
					}
				}
			}
		}
		return dataMap;
	}
	
	public static void put(String key, String value){
		if(ConfigHandler.getRequestEncode()){
			value = encodingParam(value);
		}
		ActionContext.getContext().put(key, value);
	}
	
	private static String getBodyString(HttpServletRequest request){
		String line, bodyData = "";
		try {
			BufferedReader br = request.getReader();
			while((line = br.readLine()) != null){
				bodyData += line;
			}
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return bodyData;
	}

	private static String encodingParam(String value) {
		try {
			byte[] b = value.getBytes(CodeType.ISO88591.getValue());
			value = new String(b, ConfigHandler.getContextEncode());
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return value;
	}
}
