package org.javaosc.framework.assist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static Map<String,ConvertItem> convertItemCache = new HashMap<String,ConvertItem>(); 
	
    /** 
     * Map转换为Entry 
     * @param <T> 
     * @param valueMap 泛型类型为<String,Object> 
     * @param entityClass 
     * @param prefix 从map中取值的key为   prefix + attr 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public static <T> T convert(Map valueMap,Class<T> entityClass,String prefix){ 
    	
        ConvertItem convertItem = convertItemCache.get(entityClass.getName());  
        if(convertItem == null){  
            convertItem = ConvertItem.createConvertItem(entityClass);  
            convertItemCache.put(entityClass.getName(), convertItem);  
        }  
          
        //entityClass 的可访问字段名  
        List<String> fieldNameList = convertItem.getFieldNameList();  
        //字段名和对应的set方法映射  
        Map<String,Method> fieldSetMethodMap = convertItem.getFieldSetMethodMap();  
          
        T entity = null;  
        try {  
            entity = entityClass.newInstance();  
        } catch (InstantiationException e) {  
            log.error("create "+entityClass.getName()+" instance failed, Do it has a empty constructed function ?", e);  
            return null;  
        } catch (IllegalAccessException e) {  
            log.error("create "+entityClass.getName()+" instance failed, Do it has a empty constructed function ?", e);  
            return null;  
        }  
          
        Object fieldValue = null;  
        Method m;  
        Class<?>[] parameterTypes;  
        Object targetValue;  
        if(prefix == null) prefix = "";  
        //遍历字段列表，分别调用每个字段的set方法  
        for(String fieldName: fieldNameList){  
            fieldValue = valueMap.get(prefix+fieldName);  
            if(fieldValue == null) continue;  
            m = fieldSetMethodMap.get(fieldName);  
            if(m == null) continue;  
              
            //set方法的参数类型  
            parameterTypes = m.getParameterTypes();  
            if(parameterTypes.length != 1) continue;  //只支持单个参数的set方法  
          
            //如果第一个参数类型和当前类型相同，则直接使用  
            if(parameterTypes[0].isAssignableFrom(fieldValue.getClass())){  
                targetValue = fieldValue;  
            }else{  
                //转换当前的值为目标参数类型  
                targetValue = ConvertFactory.convert(parameterTypes[0], fieldValue);  
            }  
              
            if(targetValue != null){  
                try {  
                    //调用set方法进行赋值  
                    m.invoke(entity, targetValue);  
                } catch (Exception e) {  
                    log.error("set value failed:{method="+m.getName()+",value="+targetValue+"}", e);  
                }  
            }  
        }  
        return entity;  
    }  
      
    static class ConvertItem{  
    	
        private List<String> fieldNameList = new ArrayList<String>();  
        private Map<String,Method> fieldSetMethodMap = new HashMap<String, Method>();  
          
        private ConvertItem(){}  
          
        public List<String> getFieldNameList() {  
            return fieldNameList;  
        }  
  
        public Map<String, Method> getFieldSetMethodMap() {  
            return fieldSetMethodMap;  
        }  
          
        private void parseEntry(Class<?> cls){  
            Field[] allField = cls.getDeclaredFields();  
            Method m = null;  
            String methodName;  
            for(Field f: allField){  
                methodName = f.getName();  
                methodName = "set"+methodName.substring(0, 1).toUpperCase()+methodName.substring(1);  
                try {  
                    //只返回和当前字段类型相符的set方法，不支持多参数以及不同类型的set方法  
                    m = cls.getDeclaredMethod(methodName, f.getType());  
                    if(m != null){  
                        fieldNameList.add(f.getName());  
                        fieldSetMethodMap.put(f.getName(), m);  
                    }  
                } catch (SecurityException e) {  
                    log.error("parseEntry failed: SecurityException", e);  
                } catch (NoSuchMethodException e) {  
                    log.info("NoSuchMethod in "+cls.getName()+": "+methodName);  
                }  
            }  
              
        }  
  
        public static ConvertItem createConvertItem(Class<?> cls){  
            ConvertItem ci = new ConvertItem();  
            ci.parseEntry(cls);  
            return ci;  
        }  
    }  
	
}
