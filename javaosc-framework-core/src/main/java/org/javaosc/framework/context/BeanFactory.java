package org.javaosc.framework.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.constant.Constant;
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
	
	public static <T> T getService(ProxyMode mode, Class<T> cls){
		Service service = cls.getAnnotation(Service.class);
		ScanAnnotation.setServiceField(cls);
		return get(mode, cls, true, true);
	}
	
	public static <T> T getDao(ProxyMode mode, Class<T> cls){
		return get(mode, cls, false, true);
	}
	
	public static <T> T getBean(ProxyMode mode, Class<T> cls){
		return get(mode, cls, false, true);
	}
	
	public static <T> T getBean(ProxyMode mode, Class<T> cls, boolean isCache){
		return get(mode, cls, false, isCache);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T get(ProxyMode mode, Class<T> cls , boolean isTransaction, boolean isCache){
		 Object serviceBean = null;
		 if (beanMap.containsKey(cls.getName())) { 
			 serviceBean = beanMap.get(cls.getName());
			 return (T) serviceBean;
	     }
		 try {
			 if(ProxyMode.CGLIB.equals(mode)){
				 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(cls, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance();    
			 }else{
				 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(cls, isTransaction);    
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
	
}
