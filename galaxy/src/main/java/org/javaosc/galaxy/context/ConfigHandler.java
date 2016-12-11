package org.javaosc.galaxy.context;
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

import org.javaosc.galaxy.constant.Configuration;
import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.PatternValue;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.javaosc.galaxy.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ConfigHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigHandler.class);
	
	protected static String galaxyConfig = "configuration.properties";
	
	private static Properties properties;
	
	private static String prefix;
	
	private static String suffix;
	
	private static String encoding;
	
	private static Boolean requestEncoding;
	
	private static Boolean responseEncoding;
	
	private static String[] methodKeyword;
	
	private static HashMap<String, String> viewMap;
	
	private static HashMap<String, Object> poolMap;
	
	public static void load(String galaxyCustConfig){
		
		if(!GalaxyUtil.isEmpty(galaxyCustConfig)){
			galaxyConfig = GalaxyUtil.clearSpace(galaxyCustConfig, PatternValue.ALL);
		}else{
			log.warn("context-param: galaxyConfig is missing in the web.xml. Enable default galaxyConfig: {}.", galaxyConfig);
		}
		
		InputStream inputStream = null;
		try {
			properties = new Properties();
			inputStream = new FileInputStream(PathUtil.getClassPath() + Constant.LINE + galaxyConfig);
			properties.load(inputStream);
			initConfiguration();
			log.info("galaxy configuration is initialized. filename: {}.", galaxyConfig);
		} catch (FileNotFoundException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}finally{
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			}	
		}		
	}
	
	public static String getViewPrefix(){
		if(GalaxyUtil.isEmpty(prefix)){
			prefix = getValue(Configuration.PREFIX_KEY,Configuration.DEFAULT_PREFIX_VALUE);
		}
		return prefix;
	}
	
	public static String getViewSuffix(){
		if(GalaxyUtil.isEmpty(suffix)){
			suffix = getValue(Configuration.SUFFIX_KEY,Configuration.DEFAULT_SUFFIX_VALUE);
		}
		return suffix;
	}
	
	public static String getContextEncode(){
		if(GalaxyUtil.isEmpty(encoding)){
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
	
	public static String[] getMethodKeyword(){
		if(methodKeyword==null){
			String keyword = ConfigHandler.getValue(Configuration.METHOD_KEYWORD_KEY, null);
			if(!GalaxyUtil.isEmpty(keyword)){
				keyword = GalaxyUtil.clearSpace(keyword, PatternValue.ALL).toLowerCase();
				if(keyword.indexOf(Constant.COMMA)!=-1){
					methodKeyword = keyword.split(Constant.COMMA);
				}else{
					methodKeyword = new String[]{keyword};
				}
			}else{
				methodKeyword = new String[0];
			}
		}
		return methodKeyword;
	}
	
	public static String getViewMap(String url){
		String viewUrl = null;
		if(!GalaxyUtil.isEmpty(url)){
			viewUrl = viewMap.get(url);
		}
		return viewUrl==null?"":viewUrl;
	}
	
	public static String getScanPackage(){
		return getValue(Configuration.SCANER_PACKAGE_KEY, null);
	}
	
	public static String getDataSourceName(){
		return getValue(Configuration.POOL_DATASOURCE, null);
	}
	
	protected static String getValue(String key,String defaultValue){
		String value = properties.getProperty(key);
		return !GalaxyUtil.isEmpty(value)?GalaxyUtil.clearSpace(value, PatternValue.ALL):defaultValue;
	}
	
	public static Map<String, Object> getPoolParam(){
		return poolMap;
	}
	
	public static void exprot(){
		if(properties != null){
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(PathUtil.getClassPath() + Constant.LINE + galaxyConfig);
				properties.store(outputStream, Configuration.CONFIG_HEAD_COMMENT);
				outputStream.flush();
				log.info("exporting configuration file: {}", galaxyConfig);
			} catch (FileNotFoundException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			} catch (IOException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
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
	
	private static void initConfiguration(){
		
		getViewPrefix();
		getViewSuffix();
		getContextEncode();
		getRequestEncode();
		getResponseEncode();
		getMethodKeyword();
		
		poolMap = new HashMap<String, Object>();
		viewMap = new HashMap<String, String>();
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while(it.hasNext()){
			Entry<Object, Object> entry = it.next();
			String key = String.valueOf(entry.getKey());
			if(!GalaxyUtil.isEmpty(key)){
				String value = String.valueOf(entry.getValue());
				if(!GalaxyUtil.isEmpty(value)){
					value = GalaxyUtil.clearSpace(value, PatternValue.ALL);
					if(key.startsWith(Configuration.STARTWITH_DB)){
						poolMap.put(key.replace(Configuration.STARTWITH_DB, Constant.EMPTY), value);
					}else if(key.startsWith(Configuration.STARTWITH_POOL)){
						poolMap.put(key.replace(Configuration.STARTWITH_POOL, Constant.EMPTY), value);
					}else if(key.startsWith(Configuration.VIEW_KEY)){
						String[] urlView = value.split(Constant.JZ);
						if(urlView.length==2){
							viewMap.put(urlView[0], urlView[1]);
						}else{
							log.error(Constant.GALAXY_EXCEPTION, "galaxy.url.* setting must use # mark [ps: galaxy.url.user.detail = /user/detail#/customer/detail]");
						}
					}
				}
			}
		}
	}
	
	public static void clear(){
		properties.clear();
		properties = null;
	}
		
}

