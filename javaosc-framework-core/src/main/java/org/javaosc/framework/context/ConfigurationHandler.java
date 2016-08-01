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
import org.javaosc.framework.constant.Configuration;
import org.javaosc.framework.constant.Constant.ProxyMode;
import org.javaosc.framework.util.PathUtil;
import org.javaosc.framework.util.StringUtil;
import org.javaosc.framework.util.StringUtil.PatternValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ConfigurationHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigurationHandler.class);
	
	protected static String javaoscConfig = "configuration.properties";
	
	private static Properties properties;
	
	private static String prefix;
	
	private static String suffix;
	
	private static String encoding;
	
	private static Boolean requestEncoding;
	
	private static Boolean responseEncoding;
	
	public static void load(String javaoscCustConfig){
		
		if(StringUtil.isNotBlank(javaoscCustConfig)){
			javaoscConfig = StringUtil.clearSpace(javaoscCustConfig, PatternValue.ALL);
		}else{
			log.warn("context-param: javaoscConfig is missing in the web.xml. Enable default javaoscConfig: {}.", javaoscConfig);
		}
		
		InputStream inputStream = null;
		try {
			properties = new Properties();
			inputStream = new FileInputStream(PathUtil.getClassPath() + javaoscConfig);
			properties.load(inputStream);	
			log.info("javaosc configuration is initialized. filename: {}.", javaoscConfig);
		} catch (FileNotFoundException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		} catch (IOException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}finally{
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			}	
		}		
	}
	
	public static void setValue(String key,String value){
		properties.setProperty(key, value);
	}
	
	public static String getViewPrefix(){
		if(StringUtil.isBlank(prefix)){
			prefix = getValue(Configuration.PREFIX_KEY,Configuration.DEFAULT_PREFIX_VALUE);
		}
		return prefix;
	}
	
	public static String getViewSuffix(){
		if(StringUtil.isBlank(suffix)){
			suffix = getValue(Configuration.SUFFIX_KEY,Configuration.DEFAULT_SUFFIX_VALUE);
		}
		return suffix;
	}
	
	public static String getContextEncode(){
		if(StringUtil.isBlank(encoding)){
			encoding = getValue(Configuration.CONTEXT_ENCODE_KEY,Configuration.DEFAULT_ENCODING_VALUE);
		}
		return encoding;
	}
	
	public static boolean getRequestEncode(){
		if(requestEncoding == null){
			requestEncoding = Boolean.valueOf(getValue(Configuration.REQUEST_ENCODE_KEY,Configuration.DEFAULT_ENCODING_FLAG));
		}
		return requestEncoding;
	}
	
	public static boolean getResponseEncode(){
		if(responseEncoding == null){
			responseEncoding = Boolean.valueOf(getValue(Configuration.RESPONSE_ENCODE_KEY,Configuration.DEFAULT_ENCODING_FLAG));
		}
		return responseEncoding;
	}
	
	public static String getScanPackage(){
		return getValue(Configuration.SCANER_PACKAGE_KEY, null);
	}
	
	public static String getPollName(){
		return getValue(Configuration.POOL_DATASOURCE, null);
	}
	
	public static String[] getKeywords(){
		String[] keywords = null;
		String keyword = ConfigurationHandler.getValue(Configuration.METHOD_KEYWORD_KEY, null);
		if(keyword != null){
			if(keyword.indexOf(Constant.COMMA)!=-1){
				keywords = keyword.split(Constant.COMMA);
			}else{
				keywords = new String[]{keyword};
			}
		}else{
			keywords = null;
		}
		return keywords;
	}
	
	public static String getProxyMode(){
		return ConfigurationHandler.getValue(Configuration.DYNAMIC_PROXY_KEY, ProxyMode.DEFAULT.getValue());
	}
	
	
	public static String getValue(String key){
		return properties.getProperty(key);
	}
	
	public static String getValue(String key,String defaultValue){
		String value = properties.getProperty(key);
		return StringUtil.isNotBlank(value)?StringUtil.clearSpace(value, PatternValue.ALL):defaultValue;
	}
//	
//	public static boolean getValue(String key, boolean defaultValue) {
//		String value = getValue(key);
//		return value != null ? Boolean.parseBoolean(value) : defaultValue;
//	}
//	
//	public static long getValue(String key, long defaultValue) {
//		String value = getValue(key);
//		return value != null ? Long.parseLong(value) : defaultValue;
//	}
//	
//	public static int getValue(String key, int defaultValue) {
//		String value = getValue(key);
//		return value != null ? Integer.parseInt(value) : defaultValue;
//	}
//	
	public static Map<String, Object> getPoolParam(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while(it.hasNext()){
			Entry<Object, Object> entry = it.next();
			String key = String.valueOf(entry.getKey());
			if(StringUtil.isNotBlank(key)){
				Object value = entry.getValue();
				if(key.startsWith(Configuration.STARTWITH_DB)){
					paramMap.put(key.replace(Configuration.STARTWITH_DB, Constant.EMPTY), value);
				}else if(key.startsWith(Configuration.STARTWITH_POOL)){
					paramMap.put(key.replace(Configuration.STARTWITH_POOL, Constant.EMPTY), value);
				}
			}
		}
		return paramMap;
	}
	
	public static void exprot(){
		if(properties != null){
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(PathUtil.getClassPath() + javaoscConfig);
				properties.store(outputStream, Configuration.CONFIG_HEAD_COMMENT);
				outputStream.flush();
				log.info("exporting configuration file: {}", javaoscConfig);
			} catch (FileNotFoundException e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			} catch (IOException e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
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

