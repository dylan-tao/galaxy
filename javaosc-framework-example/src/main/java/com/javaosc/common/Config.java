//package com.javaosc.common;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Properties;
//import java.util.logging.Logger;
//
//
///**
// * 
// * @description 读写通用properties配置
// * @author Dylan Tao
// * @date 2014-09-09
// * Copyright 2014 javaosc.com Team. All Rights Reserved.
// */
//public class Config {
//	
//	private static final Logger log = LoggerFactory.getLogger(Config.class);
//	
//	protected static String configFileName = "javaosc.properties";
//	
//	private static Properties properties;
//	
//	static {
//		load();
//	}
//	
//	public static void setValue(String key,String value){
//		properties.setProperty(key, value);
//	}
//	
//	public static String getValue(String key){
//		return properties.getProperty(key);
//	}
//	
//	public static String getValue(String key,String defaultValue){
//		String value = properties.getProperty(key);
//		return StringUtil.isNotBlank(value)?value:defaultValue;
//	}
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
//	public static void exprot(){
//		if(properties != null){
//			OutputStream outputStream = null;
//			try {
//				String configPath = PathUtil.getClassPath() + configFileName;
//				outputStream = new FileOutputStream(configPath);
//				properties.store(outputStream, Constant.CONFIG_NAME);
//				outputStream.flush();
//				log.info("Exporting " + configFileName);
//			} catch (FileNotFoundException e) {
//				log.error(configFileName + " can not be found,please check the file name and the file path cannot contain spaces or see the following Caused by: !",e);
//			} catch (IOException e) {
//				log.error(e);
//			} finally {
//				try {
//					if (outputStream != null) {
//						outputStream.close();
//					}
//				} catch (Exception e) {
//					log.error(e);
//				}
//			}
//		}
//	}
//	
//	private static void load(){
//		if(properties == null){
//			InputStream inputStream = null;
//			try {
//				properties = new Properties();
//				String configPath = PathUtil.getClassPath() + configFileName;
//				inputStream = new FileInputStream(configPath);
//				properties.load(inputStream);	
//				log.info("Initializing " + configFileName);
//			} catch (FileNotFoundException e) {
//				log.error(configFileName + " can not be found,please check the file name and the file path cannot contain spaces or see the following Caused by: !",e);
//			} catch (IOException e) {
//				log.error(e);
//			}finally{
//				try {
//					if(inputStream != null) {
//						inputStream.close();
//					}
//				} catch (IOException e) {
//					log.error(e);
//				}	
//			}	
//		}
//	}
//}
//
