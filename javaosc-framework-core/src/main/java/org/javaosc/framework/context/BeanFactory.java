package org.javaosc.framework.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.util.StringUtil;
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
	
//	public static <T> T getService(Class<T> cls){
//		return get(cls, true, true);
//	}
//	
//	public static <T> T getDao(Class<T> cls){
//		return get(cls, false, true);
//	}
//	
//	public static <T> T getBean(Class<T> cls){
//		return get(cls, false, true);
//	}
//	
//	public static <T> T getBean(Class<T> cls, boolean isCache){
//		return get(cls, false, isCache);
//	}
	
	public static String getKey(String custKey, Class<?> implCls){
		if(StringUtil.isBlank(custKey)){
			Class<?>[] interCls = implCls.getInterfaces();
			if(interCls!=null && interCls.length==1){ //jdk proxy key
				custKey = StringUtil.formatFirstChar(interCls[0].getSimpleName(), true);
			}else{ //cglib proxy key
				custKey = StringUtil.formatFirstChar(implCls.getSimpleName(), true);
			}
		}
		return custKey;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized <T> T get(String key, Class<T> cls , boolean isTransaction, boolean isCache){
		 Object serviceBean = null;
		 key = getKey(key, cls);
		 if (beanMap.containsKey(key)) { 
			 serviceBean = beanMap.get(key);
			 return (T) serviceBean;
	     }
		 try {
			 if(key.equalsIgnoreCase(cls.getSimpleName())){//cglib
				 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(cls, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance(); 
			 }else{  //jdk
				 Object proxy = ScanAnnotation.setServiceField(cls);
				 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(proxy, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance();
			 }
             if(isCache && StringUtil.isNotBlank(key)){
            	 beanMap.put(key, serviceBean);
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
	
}
