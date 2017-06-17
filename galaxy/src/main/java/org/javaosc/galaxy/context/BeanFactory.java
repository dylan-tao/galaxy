package org.javaosc.galaxy.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.galaxy.assist.ClassHandler;
import org.javaosc.galaxy.util.GalaxyUtil;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanFactory {
	
	public static Map<String, Object> beanMap = new HashMap<String, Object>();
	
	public static Object get(String key, Class<?> cls , boolean openConnection){
		 Object result = null;
		 if (beanMap.containsKey(key)) { 
			 result = beanMap.get(key);
			 return result;
	     }
		 Object instance = ClassHandler.newInstance(cls);
		 
		 instance = ScanAnnotation.setServiceField(cls, instance);
		 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(instance, openConnection);    
		 result = proxyHandler.proxyInstance();
		
         if(!GalaxyUtil.isEmpty(key) && result!=null){
        	 beanMap.put(key, result);
         }  	 	  
	     return result;
	} 
	
	public static void clear(){
		beanMap.clear();
		beanMap = null;
	}
	
}
