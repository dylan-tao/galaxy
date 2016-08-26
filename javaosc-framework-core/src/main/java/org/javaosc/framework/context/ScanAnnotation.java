package org.javaosc.framework.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.javaosc.framework.annotation.Bean;
import org.javaosc.framework.annotation.Dao;
import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Prototype;
import org.javaosc.framework.annotation.Autowired;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.annotation.Value;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.convert.ConvertFactory;
import org.javaosc.framework.util.StringUtil;
import org.javaosc.framework.web.RouteNodeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ScanAnnotation {
	
	private static final Logger log = LoggerFactory.getLogger(ScanAnnotation.class);
	
	protected static Object check(String key, Class<?> loadClass) {
		Object object = null;
		if(loadClass.isAnnotationPresent(Bean.class)){
			Bean bean = loadClass.getAnnotation(Bean.class);
			object = BeanFactory.get(bean.value(), loadClass, false, true);
		}else if(loadClass.isAnnotationPresent(Dao.class)){
			Dao dao = loadClass.getAnnotation(Dao.class);
			object = BeanFactory.get(dao.value(), loadClass, false, true);
		}else if(loadClass.isAnnotationPresent(Service.class)){
			Service service = loadClass.getAnnotation(Service.class);
			object = BeanFactory.get(service.value(), loadClass, true, true);
		}else{
			boolean isParentMapping = loadClass.isAnnotationPresent(Mapping.class);
			String parentPath = "";
			if(isParentMapping){
				boolean isPrototype = loadClass.isAnnotationPresent(Prototype.class);
				if(!isPrototype && isParentMapping){
					object = BeanFactory.get(StringUtil.formatFirstChar(loadClass.getSimpleName(), true), loadClass, false, true);
				}	
				
				Mapping parentMapping = loadClass.getAnnotation(Mapping.class);
				parentPath = parentMapping.value();
				
				Method[] methods= loadClass.getDeclaredMethods(); 
				for (Method method : methods) {
					boolean isSubMapping = method.isAnnotationPresent(Mapping.class);
				    if (isSubMapping) {
				    	Mapping mapping = method.getAnnotation(Mapping.class);
				    	String childPath = mapping.value();
						RouteNodeRegistry.registerRouteNode(parentPath + childPath, loadClass, method);
				    }
				}
			}
		}
//		log.info("class annotation scan is completed.");
		return object;
	}
	
	public static Object setServiceField(Class<?> cls){
		Object object = null;
		try {
			object = cls.newInstance();
		}  catch (Exception e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
		}
		 Field[] fields = cls.getDeclaredFields();
	     for(int i=0; i<fields.length;i++){  
	    	 Field field = fields[i];
	    	 field.setAccessible(true);
		     if(field.isAnnotationPresent(Autowired.class)){
		    	 Autowired ref = field.getAnnotation(Autowired.class);
		    	 Class<?> valueType = field.getType();
		    	 String refValue = ref.value();
		    	 Object proxyObj = check(refValue, valueType);
		         try {
		        	field.set(object, proxyObj);
				 } catch (Exception e) {
					 log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
				 }
		     }else if(field.isAnnotationPresent(Value.class)){
		    	 Value value = field.getAnnotation(Value.class);
		    	 Class<?> valueType = field.getType();
		    	 String refValue = value.value();
		    	 try {
		    		 if(StringUtil.isNotBlank(refValue)){
		    			field.set(object, ConvertFactory.convert(valueType, refValue));
		    		 }
		    	 } catch (Exception e) {
		    		 log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
				 }	
		     }
	     }
	     return object;
	}
}
