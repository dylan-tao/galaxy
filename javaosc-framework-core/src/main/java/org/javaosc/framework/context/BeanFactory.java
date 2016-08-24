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
	
	@SuppressWarnings("unchecked")
	public static synchronized <T> T get(String key, Class<T> cls , boolean isTransaction, boolean isCache){
		 Object serviceBean = null;
		 try {
			 Class<?>[] ifCls = cls.getInterfaces();
			 if(ifCls!=null && ifCls.length==1){ //jdk
				 key = StringUtil.isNotBlank(key)?key : StringUtil.formatFirstChar(ifCls[0].getSimpleName(), true);
				 if (beanMap.containsKey(key)) { 
					 serviceBean = beanMap.get(key);
					 return (T) serviceBean;
			     }
				 
				 Object proxy = ScanAnnotation.setServiceField(cls);
				 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(proxy, isTransaction);    
	             serviceBean = proxyHandler.proxyInstance();
	           
			 }else{ //cglib
				 key = StringUtil.isNotBlank(key)?key : StringUtil.formatFirstChar(cls.getSimpleName(), true);
				 if (beanMap.containsKey(cls.getName())) { 
					 serviceBean = beanMap.get(cls.getName());
					 return (T) serviceBean;
			     }
				 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(cls, isTransaction);    
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
