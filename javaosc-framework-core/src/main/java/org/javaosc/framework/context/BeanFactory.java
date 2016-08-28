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
	
	protected static String getKey(String custKey, Class<?> implCls){
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
	
	public static synchronized Object get(String key, Class<?> cls , boolean isTransaction){
		 Object instBean = null;
//		 key = getKey(key, cls);
		 if (beanMap.containsKey(key)) { 
			 instBean = beanMap.get(key);
			 return instBean;
	     }
		 Object proxy = ScanAnnotation.setServiceField(cls);
		 try {
			 if(key.equalsIgnoreCase(cls.getSimpleName())){//cglib
				 ProxyCglibHandler proxyHandler = new ProxyCglibHandler(proxy, isTransaction);    
				 instBean = proxyHandler.proxyInstance(); 
			 }else{  //jdk
				 ProxyJdkHandler proxyHandler = new ProxyJdkHandler(proxy, isTransaction);    
				 instBean = proxyHandler.proxyInstance();
			 }
             if(StringUtil.isNotBlank(key) && instBean!=null){
            	 beanMap.put(key, instBean);
             }  	  
	     } catch (Exception e) {    
	    	 log.error(Constant.JAVAOSC_EXCEPTION, e); 
	     } 
	     return instBean;
	} 
	
	public static void clear(){
		beanMap.clear();
		beanMap = null;
	}
	
}
