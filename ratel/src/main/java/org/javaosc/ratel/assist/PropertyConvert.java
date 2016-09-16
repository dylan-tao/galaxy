package org.javaosc.ratel.assist;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
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
		Map<String, Method> propertyMap = getPropertyMap(entityClass);
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
            		if(entity==null || value==null){ continue; }
            		
            		if(targetType.isAssignableFrom(value.getClass())){
            			targetValue = value;
            		}else{
            			targetValue = ConvertFactory.convert(targetType,value);
            		}
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
	
	
	@SuppressWarnings("unchecked")
	public static <T> T convertResultSetToEntity(ResultSet rs, Class<T> entityClass) throws SQLException {  
		T entity = null;
		Map<String, Method> propertyMap = getPropertyMap(entityClass);
        if(propertyMap.size()>0){
        	try {
				entity = entityClass.newInstance();
			} catch (Exception e) {
				log.error(Constant.RATEL_EXCEPTION, e);
			} 
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int cols = rsmd.getColumnCount();
        	
            for(int col = 1; col <= cols; col++){
            	
            	String columnName = rsmd.getColumnLabel(col);
                if (StringUtil.isBlank(columnName)) {
                    columnName = rsmd.getColumnName(col);
                }
            	
            	if (propertyMap.containsKey(columnName)) {
            		Object targetValue = null;
            		Object value = null;
            		Method m = propertyMap.get(columnName);
            		Class<?> targetType = m.getParameterTypes()[0];
            		
            		if (targetType.equals(Timestamp.class)) {
            			value = rs.getTimestamp(col);
                    } else if (targetType.equals(SQLXML.class)) {
                    	value = rs.getSQLXML(col);
                    } else {
                    	value = rs.getObject(col);
                    }
            		
            		if(entity==null || value==null){ continue; }
            		
            		if (value instanceof java.util.Date) {
                         final String targetTypeName = targetType.getName();
                         if ("java.sql.Date".equals(targetTypeName)) {
                        	 targetValue = new java.sql.Date(((java.util.Date) value).getTime());
                         } else
                         if ("java.sql.Time".equals(targetTypeName)) {
                        	 targetValue = new java.sql.Time(((java.util.Date) value).getTime());
                         } else
                         if ("java.sql.Timestamp".equals(targetTypeName)) {
                             Timestamp tsValue = (Timestamp) value;
                             int nanos = tsValue.getNanos();
                             targetValue = new java.sql.Timestamp(tsValue.getTime());
                             ((Timestamp) targetValue).setNanos(nanos);
                         }
                    }else if (value instanceof String && targetType.isEnum()) {
                    	targetValue = Enum.valueOf(targetType.asSubclass(Enum.class), (String)value);
                    }else if(targetType.isAssignableFrom(value.getClass())){
            			targetValue = value;
            		}else{
            			targetValue = ConvertFactory.convert(targetType,value);
            		}
            		
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
	
	private static Map<String, Method> getPropertyMap(Class<?> entityClass){
		Map<String, Method> propertyMap = fieldSetPropertyMap.get(entityClass.getName());  
        if(propertyMap == null){  
        	propertyMap = parseEntry(entityClass);  
        }
        return propertyMap;
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
