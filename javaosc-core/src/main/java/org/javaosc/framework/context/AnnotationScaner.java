package org.javaosc.framework.context;

import java.lang.reflect.Method;
import java.util.List;

import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Prototype;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
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
public class AnnotationScaner {
	
	private static final Logger log = LoggerFactory.getLogger(AnnotationScaner.class);
	
	public void load(){
		String packageName = Configuration.getValue(ProperConstant.SCANER_PACKAGE_KEY);
		List<String> className = new ScanPackage().getClassName(packageName);
		scanerClassFile(className);
		log.info("Initializing class Annotation");
		className = null;
	}
	
	private void scanerClassFile(List<String> className) {
		for(String cn : className){
			Class<?> loadClass = org.javaosc.framework.assist.ClassHandler.load(cn);
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
	}
	
}
