package org.javaosc.framework.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.ContentType;
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
public class ActionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ActionHandler.class);
	
	public static void response(String content) {
		response(content,ContentType.JSON);
	}
	
	public static void response(String content,ContentType contentType) {
		HttpServletResponse response = ActionContext.getContext().getResponse();
		response.setContentType(contentType.getValue());
		try {
			PrintWriter out = response.getWriter();
			out.write(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			log.debug(content);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void renewPrmRecord(){
		HttpServletRequest request = ActionContext.getContext().getRequest();
		Map<String, Object> map = request.getParameterMap();
		for(Map.Entry<String, Object> entry : map.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			request.setAttribute(key, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void renewPrmRecord(String... parameter){
		HttpServletRequest request = ActionContext.getContext().getRequest();
		Map<String, Object> map = request.getParameterMap();
		for(String para : parameter){
			request.setAttribute(para, map.get(para));
		}
	}
	
	protected static String redirectResult(boolean isOne, String key, Object value, Map<String, Object> returnData){
		StringBuffer returnStr = new StringBuffer(Constant.EMPTY);
		if(isOne){
			if(StringUtil.isNotBlank(key)){
				returnStr.append(Constant.QM).append(key).append(Constant.EM).append(value);
			}
		}else{
			if(returnData != null){
				Iterator<Entry<String, Object>> iterator = returnData.entrySet().iterator();
				boolean firstIndex = true;
				while(iterator.hasNext()){
					Entry<String, Object> entry = iterator.next();
					if(firstIndex){
						returnStr.append(Constant.QM).append(entry.getKey()).append(Constant.EM).append(entry.getValue());
						firstIndex = false;
					}else{
						returnStr.append(Constant.AM).append(entry.getKey()).append(Constant.EM).append(entry.getValue());
					}
				}
			}
		}
		return returnStr.toString();
	}
	
}
