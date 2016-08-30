package org.javaosc.framework.convert;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.cglib.beans.BeanMap;

import org.javaosc.framework.assist.PropertyConvert;
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
public class CglibConvert {
		
		private static final Logger log = LoggerFactory.getLogger(PropertyConvert.class);
		
		private static Map<String, Map<String,Class<?>>> fieldSetPropertyMap = new HashMap<String, Map<String,Class<?>>>();
		
		public static <T> T convertMapToEntity(@SuppressWarnings("rawtypes") Map map, Class<T> entityClass) {  
			
			T entity = null;
			Map<String, Class<?>> propertyMap = fieldSetPropertyMap.get(entityClass.getName());  
	    	
	        if(propertyMap == null){  
	        	propertyMap = parseEntry(entityClass);  
	        }  
	        BeanMap beanMap = null;
	        if(propertyMap.size()>0){
	        	try {
					entity = entityClass.newInstance();
					beanMap = BeanMap.create(entity);  
				} catch (Exception e) {
					log.error(Constant.JAVAOSC_EXCEPTION, e);
				} 
	            for(Entry<String, Class<?>> entry:propertyMap.entrySet()){
	            	String name = entry.getKey();
	            	if (map.containsKey(name)) {
	            		Object targetValue;
	            		Class<?> targetType = entry.getValue();
	            		Object value = map.get(name);
	            		if(targetType.isAssignableFrom(value.getClass())){
	            			targetValue = value;
	            		}else{
	            			targetValue = ConvertFactory.convert(targetType,value);
	            		}
	            		if(entity==null || beanMap==null){ return null; }
	            		beanMap.put(entity, name, targetValue);
	            	}
	            }  
	        }
	        return entity;
		}
	  
	    private static Map<String, Class<?>> parseEntry(Class<?> clsType){  
			BeanInfo beanInfo = null;
			try {
				beanInfo = Introspector.getBeanInfo(clsType);
			} catch (Exception e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			} 
			Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
			if(beanInfo!=null){
				 PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); 
			        for (int i = 0; i < propertyDescriptors.length; i++) {  
			            PropertyDescriptor property = propertyDescriptors[i];
			            Method m = property.getWriteMethod();
			            if(m!=null){
			            	Class<?>[] paramTypes = m.getParameterTypes();  
			                if(paramTypes.length != 1) continue;
			                
			                String name = property.getName();  
			                Class<?> type = property.getPropertyType();
			                propertyMap.put(name, type);  
			            }
			        }
			        if(propertyMap.size()>0){
			        	fieldSetPropertyMap.put(clsType.getName(), propertyMap);
			        }
			}
	        return propertyMap;
	    }  
	    
}
