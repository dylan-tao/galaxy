package com.javaosc.common.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.javaosc.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @description Json帮助类
 * @author Dylan Tao
 * @date 2014-10-26
 * Copyright 2014 javaosc.com Team. All Rights Reserved.
 */

public class JsonUtil {

	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new GsonBuilder().serializeNulls().create();
		}
	}

	/**
	 * 将对象转为JSON
	 * @param obj 要转的对象
	 * @return String json字符串
	 */
	public static String toJSON(Object obj) {
		String json = null;
		if (obj == null) {
			json = "{}";
		}else{
			try {
				json = gson.toJson(obj);
			} catch (Exception e) {
				if (obj instanceof Collection<?> || obj.getClass().isArray() || obj instanceof Iterator<?> || obj instanceof Enumeration<?>) {
					json = "[]";
				} else{
					json = "{}";
				}	
			}
		}
		log.debug("====== JSON Content: {}", json);
		return json;
	}
	
	/**
	 * 将字符串转换为对象
	 * @param json 字符串
	 * @param cls 对象类型
	 * @return T 返回对象
	 */
	public static <T> T fromJson(String json, Class<T> cls) {
		if (StringUtil.isBlank(json)) {
			return null;
		}
		try {
			return gson.fromJson(json, cls);
		} catch (Exception ex) {
			log.error("{} 无法转换为{} 对象!",json, cls.getName());
			return null;
		}
	}
	
	/**
	 * 将字符串转换为对象
	 * @param json 字符串
	 * @param token 
	 * 转换成List<User>则为new TypeToken<List<User>>(){}
	 * 转换成PageModel<User>则为new TypeToken<Page<User>>(){}
	 * 转换成HashMap<String,List>则为new TypeToken<Map<String,List>>(){}
	 * @return T
	 */
	public static <T> T fromJson(String jsonStr) {
		if (StringUtil.isBlank(jsonStr)) {
			return null;
		}
		try {
			return gson.fromJson(jsonStr, new TypeToken<T>(){}.getType());
		} catch (Exception ex) {
			log.error("{} 无法转换为{}对象!",jsonStr, new TypeToken<T>(){}.getRawType().getName());
			return null;
		}
	}

	public static boolean isJson(String jsonStr) {
		 if(StringUtil.isBlank(jsonStr)) {    
		    return false;    
		 }    
		 try {    
		     new JsonParser().parse(jsonStr);  
		     return true;    
		 } catch (JsonParseException e) {    
			 log.error("json格式错误：{}", jsonStr);
		     return false;    
		}      
	}
	
}
