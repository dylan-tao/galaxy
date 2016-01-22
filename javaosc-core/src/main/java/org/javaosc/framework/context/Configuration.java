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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uufast.framework.web.util.PathUtil;
import org.uufast.framework.web.util.StringUtil;

/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class Configuration {
	
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	protected static String configFileName = "uufast.properties";
	
	private static Properties properties;
	
	public static void setConfigFileName(String configFileName) {
		if(StringUtil.isNotBlank(configFileName)){
			Configuration.configFileName = configFileName.trim();
		}else{
			log.error("propertie[ configFileName ] must be not null, please check setting in web.xml");
		}
	}
	
	public static void load(){
			InputStream inputStream = null;
			try {
				properties = new Properties();
				String configPath = PathUtil.getClassPath() + configFileName;
				inputStream = new FileInputStream(configPath);
				properties.load(inputStream);	
				log.info("Initializing " + configFileName);
			} catch (FileNotFoundException e) {
				log.error("[errorCode:1114] " + configFileName + " can not be found,please check the file name and the file path cannot contain spaces or see the following Caused by: !",e);
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
				String configPath = PathUtil.getClassPath() + configFileName;
				outputStream = new FileOutputStream(configPath);
				properties.store(outputStream, ProperConstant.CONFIG_HEAD_COMMENT);
				outputStream.flush();
				log.info("Exporting " + configFileName);
			} catch (FileNotFoundException e) {
				log.error("[errorCode:1114] " + configFileName + " can not be found,please check the file name and the file path cannot contain spaces or see the following Caused by: !",e);
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
		properties = null;
	}
		
}

