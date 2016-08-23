package org.javaosc.framework.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.javaosc.framework.annotation.Bean;
import org.javaosc.framework.annotation.Dao;
import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Prototype;
import org.javaosc.framework.annotation.Ref;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.annotation.Value;
import org.javaosc.framework.assist.ClassHandler;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.convert.ConvertFactory;
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
	
	protected static void check(String className) {
		Class<?> loadClass = ClassHandler.load(className);
		
		if(loadClass.isAnnotationPresent(Bean.class)){
			Bean bean = loadClass.getAnnotation(Bean.class);
		}else if(loadClass.isAnnotationPresent(Dao.class)){
			Dao dao = loadClass.getAnnotation(Dao.class);
		}else if(loadClass.isAnnotationPresent(Service.class)){
			
		}else{
			Mapping parentMapping = loadClass.getAnnotation(Mapping.class);
			String parentPath = parentMapping!=null?parentMapping.value():Constant.EMPTY;
			boolean isCache = false;
			Method[] methods= loadClass.getDeclaredMethods(); 
			for (Method method : methods) {
				Mapping mapping = method.getAnnotation(Mapping.class);
			    if (mapping != null) {
			    	String childPath = mapping.value();
					RouteNodeRegistry.registerRouteNode(parentPath + childPath, loadClass, method);
					isCache = true;
			    }
			}
			if(isCache){
				boolean isPrototype = loadClass.isAnnotationPresent(Prototype.class);
				if(!isPrototype){
//					BeanFactory.getBean(loadClass);
				}
			}
		}
		log.info("class annotation scan is completed.");
	}
	
	public static void setServiceField(Class<?> cls){
		Object clsObject = null;
		try {
			clsObject = cls.newInstance();
		}  catch (Exception e1) {
			e1.printStackTrace();
		}
		 Field[] fields = cls.getDeclaredFields();
	     for(int i=0; i<fields.length;i++){  
	    	 Field field = fields[i];
	    	 field.setAccessible(true);
		     if(field.isAnnotationPresent(Ref.class)){
//		    	 Ref ref = field.getAnnotation(Ref.class);  
//		    	 String refValue = ref.value();
//		         try {
//		        	field.set(clsObject, refValue);
//				 } catch (Exception e) {
//					e.printStackTrace();
//				 }	
		     }else if(field.isAnnotationPresent(Value.class)){
		    	 Value value = field.getAnnotation(Value.class);
		    	 Class<?> valueType = field.getType();
		    	 String refValue = value.value();
		    	 try {
		    		field.set(clsObject, ConvertFactory.convert(valueType, refValue));
		    	 } catch (Exception e) {
					e.printStackTrace();
				}	
		     }
	     }
//	     ProxyJdkHandler handler = new ProxyJdkHandler(clsObject, false);
//		 clsObject = handler.proxyInstance();
//		 UserService userService = (UserService)clsObject;
//		 System.out.println(userService.getUserName());
	}
}
