package org.javaosc.ratel.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.ratel.assist.ClassHandler;
import org.javaosc.ratel.util.StringUtil;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BeanFactory {
	
	public static Map<String, Object> beanMap = new HashMap<String, Object>();
	
	public static synchronized Object get(String key, Class<?> cls , boolean openConnection){
		 Object result = null;
		 if (beanMap.containsKey(key)) { 
			 result = beanMap.get(key);
			 return result;
	     }
		 Object format = null;
		 format = ClassHandler.newInstance(cls);
		 if(key.equalsIgnoreCase(cls.getSimpleName())){//cglib
			 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(format, openConnection);    
			 result = proxyHandler.proxyInstance(); 
			 result = ScanAnnotation.setServiceField(cls, result);
		 }else{  //jdk
			 format = ScanAnnotation.setServiceField(cls, format);
			 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(format, openConnection);    
			 result = proxyHandler.proxyInstance();
		 }
		
         if(StringUtil.isNotBlank(key) && result!=null){
        	 beanMap.put(key, result);
         }  	 	  
	     return result;
	} 
	
	public static void clear(){
		beanMap.clear();
		beanMap = null;
	}
	
}
