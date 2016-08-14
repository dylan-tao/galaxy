package org.javaosc.framework.context;

import java.lang.reflect.Method;

import org.javaosc.framework.annotation.Bean;
import org.javaosc.framework.annotation.Dao;
import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Prototype;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.assist.ClassHandler;
import org.javaosc.framework.constant.Constant;
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
		
		Service service = null;
		Dao dao = null;
		Bean bean = null;
		if((bean = loadClass.getAnnotation(Bean.class))!=null){
			
		}else if((dao = loadClass.getAnnotation(Dao.class))!=null){
			
		}else if((service = loadClass.getAnnotation(Service.class))!=null){
			
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
				Prototype prototypeMapping = loadClass.getAnnotation(Prototype.class);
				if(prototypeMapping == null){
					BeanFactory.getBean(loadClass);
				}
			}
		}
		log.info("class annotation scan is completed.");
	}
	
}
