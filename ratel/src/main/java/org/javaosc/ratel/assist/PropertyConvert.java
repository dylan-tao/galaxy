package org.javaosc.ratel.assist;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.convert.ConvertFactory;
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
public class PropertyConvert {
	
	private static final Logger log = LoggerFactory.getLogger(PropertyConvert.class);
	
	private static Map<String, Map<String,Method>> fieldSetPropertyMap = new HashMap<String, Map<String,Method>>();
	
	public static <T> T convertMapToEntity(Map<String, Object> map, Class<T> entityClass) {  
		T entity = null;
		Map<String, Method> propertyMap = fieldSetPropertyMap.get(entityClass.getName());  
    	
        if(propertyMap == null){  
        	propertyMap = parseEntry(entityClass);  
        }  
        if(propertyMap.size()>0){
        	try {
				entity = entityClass.newInstance();
			} catch (Exception e) {
				log.error(Constant.RATEL_EXCEPTION, e);
			} 
            for(Entry<String, Object> entry:map.entrySet()){
            	String name = entry.getKey(); //key
        		Method m = propertyMap.get(name);
        		if(m!=null){
        			Object targetValue;
            		Class<?> targetType = m.getParameterTypes()[0];
            		Object value = map.get(name);
            		if(targetType.isAssignableFrom(value.getClass())){
            			targetValue = value;
            		}else{
            			targetValue = ConvertFactory.convert(targetType,value);
            		}
            		if(entity==null || targetValue==null){ continue; }
            		try {
    					m.invoke(entity, new Object[]{targetValue});
    				} catch (Exception e) {
    					log.error(Constant.RATEL_EXCEPTION, e);
    				} 	
        		}
            }  
        }
        return entity;
	}
	
	public static <T> T convertResultSetToEntity(ResultSet rs, Class<T> entityClass) {  
		T entity = null;
		Map<String, Method> propertyMap = fieldSetPropertyMap.get(entityClass.getName());  
    	
        if(propertyMap == null){  
        	propertyMap = parseEntry(entityClass);  
        }  
        if(propertyMap.size()>0){
        	try {
				entity = entityClass.newInstance();
			} catch (Exception e) {
				log.error(Constant.RATEL_EXCEPTION, e);
			} 
            for(Entry<String, Method> entry:propertyMap.entrySet()){
            	String name = entry.getKey();
            	if (map.containsKey(name)) {
            		Object targetValue;
            		Method m = entry.getValue();
            		Class<?> targetType = m.getParameterTypes()[0];
            		Object value = map.get(name);
            		if(targetType.isAssignableFrom(value.getClass())){
            			targetValue = value;
            		}else{
            			targetValue = ConvertFactory.convert(targetType,value);
            		}
            		if(entity==null || targetValue==null){ continue; }
            		try {
						m.invoke(entity, new Object[]{targetValue});
					} catch (Exception e) {
						log.error(Constant.RATEL_EXCEPTION, e);
					} 
            	}
            }  
        }
        return entity;
	}
  
    private static Map<String, Method> parseEntry(Class<?> clsType){  
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clsType);
		} catch (Exception e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		} 
		Map<String, Method> propertyMap = new HashMap<String, Method>();
		if(beanInfo!=null){
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); 
	        for (int i = 0; i < propertyDescriptors.length; i++) {  
	            PropertyDescriptor property = propertyDescriptors[i];
	            Method m = property.getWriteMethod();
	            if(m!=null){
	            	Class<?>[] paramTypes = m.getParameterTypes();  
	                if(paramTypes.length != 1) continue;
	                
	                String name = property.getName();  
	                propertyMap.put(name, m);
	                
	                String underlineName = StringUtil.camelToUnderline(name);
	                //has upper has
	                if(!name.equals(underlineName)){
	                	propertyMap.put(underlineName, m);
	                }
	            }
	        }
	        if(propertyMap.size()>0){
	        	fieldSetPropertyMap.put(clsType.getName(), propertyMap);
	        }   
		}
        return propertyMap;
    }  
    
}
