package org.javaosc.framework.assist;

import org.javaosc.framework.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ClassHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ClassHandler.class);
	
	public static Class<?> load(final String name) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
			return null;
		}
	}
	
	public static boolean isJavaClass(Class<?> cls){
		return cls != null && cls.getClassLoader() == null;  
	}
	
	public static boolean isWrapClass(Class<?> clz) {     
        try {     
           return ((Class<?>)clz.getField("TYPE").get(null)).isPrimitive();    
        } catch (Exception e) {     
           return false;     
        }     
    }   
	
}
