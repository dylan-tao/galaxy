package org.javaosc.framework.assist;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

public class PropertyConvert {
	
	public static <T> T mapToBean(Map<String, Object> map, T bean) {  
	    BeanMap beanMap = BeanMap.create(bean);  
	    beanMap.putAll(map);  
	    return bean;  
	}  
	
	public static <T> Map<String, Object> beanToMap(T bean) {  
	    Map<String, Object> map = new HashMap<String, Object>();  
	    if (bean != null) {  
	        BeanMap beanMap = BeanMap.create(bean);  
	        for (Object key : beanMap.keySet()) {  
	            map.put(String.valueOf(key), beanMap.get(key));  
	        }             
	    }  
	    return map;  
	} 
	
	
}
