package org.javaosc.framework.assist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ClassHandler {
	
	public static Class<?> load(final String name) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
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
