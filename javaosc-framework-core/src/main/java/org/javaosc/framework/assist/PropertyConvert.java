package org.javaosc.framework.assist;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.convert.ConvertFactory;
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
	
	public static <T> T convertMapToEntity(Map map, Class<T> entityClass) {  
		
		T entity = null;
		Map<String, Method> propertyMap = fieldSetPropertyMap.get(entityClass.getName());  
    	
        if(propertyMap == null){  
        	propertyMap = parseEntry(entityClass);  
        }  
        if(propertyMap.size()>0){
        	try {
				entity = entityClass.newInstance();
			} catch (Exception e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
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
						m.invoke(entity, targetValue);
					} catch (Exception e) {
						e.printStackTrace();
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
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		} 
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); 
        Map<String, Method> propertyMap = new HashMap<String, Method>();
        for (int i = 0; i < propertyDescriptors.length; i++) {  
            PropertyDescriptor property = propertyDescriptors[i];
            Method m = property.getWriteMethod();
            if(m!=null){
            	Class<?>[] paramTypes = m.getParameterTypes();  
                if(paramTypes.length != 1) continue;
                
                String name = property.getName();  
                propertyMap.put(name, m);  
            }
        }
        if(propertyMap.size()>0){
        	fieldSetPropertyMap.put(clsType.getName(), propertyMap);
        }
        return propertyMap;
    }  
    
}
