package org.javaosc.framework.assist;

import java.util.Map;

import net.sf.cglib.beans.BeanMap;

public class PropertyConvert {
	
	public static <T> T mapToBean(Map<String, Object> map, T bean) {  
	    BeanMap beanMap = BeanMap.create(bean);  
	    beanMap.putAll(map);  
	    return bean;  
	}  
	
}
