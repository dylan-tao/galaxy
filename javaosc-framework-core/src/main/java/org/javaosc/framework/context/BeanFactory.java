package org.javaosc.framework.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.constant.Constant.ProxyMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanFactory {
	
	private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
	
	public static Map<String, Object> beanMap = new HashMap<String, Object>();
	
	static String[] keywords;
	
	static String proxyMode;
	
	public static <T> T getService(Class<T> cls){
		return get(cls, true, true);
	}
	
	public static <T> T getDao(Class<T> cls){
		return get(cls, false, true);
	}
	
	public static <T> T getBean(Class<T> cls){
		return get(cls, false, true);
	}
	
	public static <T> T getBean(Class<T> cls, boolean isCache){
		return get(cls, false, isCache);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T get(Class<T> cls , boolean isTransaction, boolean isCache){
		 Object serviceBean = null;
		 if (beanMap.containsKey(cls.getName())) { 
			 serviceBean = beanMap.get(cls.getName());
			 return (T) serviceBean;
	     }
		 try {
			 if(proxyMode.equals(ProxyMode.CGLIB.getValue())){
				 CglibProxyHandler proxyHandler = new CglibProxyHandler(cls, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance();    
			 }else{
				 ProxyHandler proxyHandler = new ProxyHandler(cls, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance();   
			 }
             if(isCache){
            	beanMap.put(cls.getName(), serviceBean);    
             }  	  
	     } catch (Exception e) {    
	    	 log.error(Constant.JAVAOSC_EXCEPTION, e); 
	     } 
	     return (T) serviceBean;
	} 
	
	public static void clear(){
		beanMap.clear();
		beanMap = null;
	}
	
	public static void initKeywords(){
		String keyword = Configuration.getValue(ProperConstant.METHOD_KEYWORD_KEY, null);
		if(keyword != null){
			if(keyword.indexOf(Constant.COMMA)!=-1){
				keywords = keyword.split(Constant.COMMA);
			}else{
				keywords = new String[]{keyword};
			}
		}else{
			keywords = null;
		}
		proxyMode =  Configuration.getValue(ProperConstant.DYNAMIC_PROXY_KEY, ProxyMode.DEFAULT.getValue());
	}
}
