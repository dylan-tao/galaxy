package org.javaosc.framework.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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

	public static String toJson(Object obj) {
		String json = null;
		if(obj!=null){
			try {
				json = gson.toJson(obj);
			} catch (Exception e) {
				log.error("to json error ~ {}", obj.getClass().getName());
			}
		}
		log.debug("=== JSON Content: {}", json);
		return json;
	}
	
	public static <T> T fromBean(String json, Class<T> cls) {
		T t = null;
		if(StringUtil.isNotBlank(json)){
			try {
				t = gson.fromJson(json, cls);
			} catch (Exception e) {
				log.error("{} can not format {} !",json, cls.getName());
			}
		}
		return t;
	}
	
	public static <T> List<T> fromArray(String json, Class<T> cls) {
		List<T> list = null;
		if(StringUtil.isNotBlank(json)){
			try {
				list = gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
			} catch (Exception ex) {
				log.error("{} can not format {} !", json, new TypeToken<T>(){}.getRawType().getName());
			}
		}
		return list;
	}
	
	public static <T> Map<String, T> fromMap(String json) {
		Map<String, T> map = null;
		if (StringUtil.isNotBlank(json)) {
			try {
				map = gson.fromJson(json, new TypeToken<Map<String, T>>(){}.getType());
			} catch (JsonSyntaxException e) {
				log.error("{} can not format {} !", json, new TypeToken<Map<String, T>>(){}.getRawType().getName());
			}
		}
		return map;
	}
	
	public static <T> List<Map<String, T>> fromMapArray(String json) {
		List<Map<String, T>> list = null;
		if (StringUtil.isNotBlank(json)) {
			try {
				list = gson.fromJson(json, new TypeToken<List<Map<String, T>>>(){}.getType());
			} catch (JsonSyntaxException e) {
				log.error("{} can not format {} !", json, new TypeToken<List<Map<String, T>>>(){}.getRawType().getName());
			}
		}
		return list;
	}
	
	public static boolean check(String json) {
		 if(StringUtil.isNotBlank(json)) {    
			 try {    
			     new JsonParser().parse(json);  
			     return true;    
			 } catch (JsonParseException e) {    
				 log.error("json format error ：{}", json);
			}       
		 }    
		 return false; 
	}
	
}
