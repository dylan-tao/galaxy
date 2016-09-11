package org.javaosc.ratel.context;

import java.util.HashMap;
import java.util.Map;

import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.util.StringUtil;
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
	
	public static synchronized Object get(String key, Class<?> cls , boolean isTransaction){
		 Object result = null;
		 if (beanMap.containsKey(key)) { 
			 result = beanMap.get(key);
			 return result;
	     }
		 Object format = null;
		 try {
			 format = cls.newInstance();
			 if(key.equalsIgnoreCase(cls.getSimpleName())){//cglib
				 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(format, isTransaction);    
				 result = proxyHandler.proxyInstance(); 
				 result = ScanAnnotation.setServiceField(cls, result);
			 }else{  //jdk
				 format = ScanAnnotation.setServiceField(cls, format);
				 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(format, isTransaction);    
				 result = proxyHandler.proxyInstance();
			 }
			
             if(StringUtil.isNotBlank(key) && result!=null){
            	 beanMap.put(key, result);
             }  	  
	     } catch (Exception e) {    
	    	 log.error(Constant.JAVAOSC_EXCEPTION, e); 
	     } 
	     return result;
	} 
	
	public static void clear(){
		beanMap.clear();
		beanMap = null;
	}
	
}
