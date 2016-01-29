package org.javaosc.framework.context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.web.util.PathUtil;
import org.javaosc.framework.web.util.StringUtil;
import org.javaosc.framework.web.util.StringUtil.PatternValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class Configuration {
	
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	protected static String configFileName = "configuration.properties";
	
	private static Properties properties;
	
	public static void setConfigFileName(String configFileName) {
		if(StringUtil.isNotBlank(configFileName)){
			configFileName = StringUtil.clearSpace(configFileName, PatternValue.ALL);
		}else{
			log.info("configuration file missing,enable default configuration file: {}", configFileName);
		}
	}
	
	public static void load(){
		InputStream inputStream = null;
		try {
			properties = new Properties();
			inputStream = new FileInputStream(PathUtil.getClassPath() + configFileName);
			properties.load(inputStream);	
			log.info("initializing configuration file: {}", configFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
	}
	
	public static void setValue(String key,String value){
		properties.setProperty(key, value);
	}
	
	public static String getValue(String key){
		return properties.getProperty(key);
	}
	
	public static String getValue(String key,String defaultValue){
		String value = properties.getProperty(key);
		return StringUtil.isNotBlank(value)?value:defaultValue;
	}
	
	public static boolean getValue(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.parseBoolean(value) : defaultValue;
	}
	
	public static long getValue(String key, long defaultValue) {
		String value = getValue(key);
		return value != null ? Long.parseLong(value) : defaultValue;
	}
	
	public static int getValue(String key, int defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.parseInt(value) : defaultValue;
	}
	
	public static Map<String, Object> getPoolPrm(){
		Map<String, Object> prm = new HashMap<String, Object>();
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while(it.hasNext()){
			Entry<Object, Object> entry = it.next();
			String key = (String)entry.getKey();
			if(StringUtil.isNotBlank(key)){
				Object value = entry.getValue();
				if(key.startsWith(ProperConstant.STARTWITH_DB)){
					prm.put(key.replace(ProperConstant.STARTWITH_DB, Constant.EMPTY), value);
				}else if(key.startsWith(ProperConstant.STARTWITH_POOL)){
					prm.put(key.replace(ProperConstant.STARTWITH_POOL, Constant.EMPTY), value);
				}
			}
		}
		return prm;
	}
	
	public static void exprot(){
		if(properties != null){
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(PathUtil.getClassPath() + configFileName);
				properties.store(outputStream, ProperConstant.CONFIG_HEAD_COMMENT);
				outputStream.flush();
				log.info("exporting configuration file: {}", configFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void clear(){
		properties.clear();
	}
		
}

