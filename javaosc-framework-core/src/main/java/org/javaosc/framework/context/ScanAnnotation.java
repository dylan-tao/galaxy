package org.javaosc.framework.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import org.javaosc.framework.annotation.Autowired;
import org.javaosc.framework.annotation.Bean;
import org.javaosc.framework.annotation.Dao;
import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Prototype;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.annotation.Value;
import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.convert.ConvertFactory;
import org.javaosc.framework.jdbc.JdbcHandler;
import org.javaosc.framework.util.StringUtil;
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
public class ScanAnnotation {
	
	private static final Logger log = LoggerFactory.getLogger(ScanAnnotation.class);
	
	private static HashMap<String, Class<?>> annotationMap;
	
	protected static void putAnnotation(Class<?> loadClass){
		if(annotationMap==null){
			annotationMap = new HashMap<String, Class<?>>();
			annotationMap.put("jdbcHandler", JdbcHandler.class);
		}
		String custKey = null;
		boolean flag = false;
		if(loadClass.isAnnotationPresent(Bean.class)){
			Bean bean = loadClass.getAnnotation(Bean.class);
			custKey = bean.value();
			flag = true;
		}else if(loadClass.isAnnotationPresent(Dao.class)){
			Dao dao = loadClass.getAnnotation(Dao.class);
			custKey = dao.value();
			flag = true;
		}else if(loadClass.isAnnotationPresent(Service.class)){
			Service service = loadClass.getAnnotation(Service.class);
			custKey = service.value();
			flag = true;
		}else if(loadClass.isAnnotationPresent(Mapping.class)){
			custKey = null;
			flag = true;
		}
		if(flag){
			custKey = getKey(custKey, loadClass);
			annotationMap.put(custKey, loadClass);
		}
	}
	
	public static void registryAnnotation(){
		if(annotationMap!=null){
			for(Entry<String, Class<?>> entry:annotationMap.entrySet()){
				String key = entry.getKey();
				Class<?> cls = entry.getValue();
				if(cls.isAnnotationPresent(Bean.class) || cls.isAnnotationPresent(Dao.class)){
					BeanFactory.get(key, cls, false);
				}else if(cls.isAnnotationPresent(Service.class)){
					BeanFactory.get(key, cls, true);
				}else if(cls.isAnnotationPresent(Mapping.class)){
					Object action = null;
					String parentPath = "";
					
					if(cls.isAnnotationPresent(Prototype.class)){
						action = cls;
					}else{
						action = setServiceField(cls);
					}
					
					Mapping parentMapping = cls.getAnnotation(Mapping.class);
					parentPath = parentMapping.value();
					
					Method[] methods= cls.getDeclaredMethods(); 
					for (Method method : methods) {
						boolean isSubMapping = method.isAnnotationPresent(Mapping.class);
					    if (isSubMapping) {
					    	Mapping mapping = method.getAnnotation(Mapping.class);
					    	String childPath = mapping.value();
							RouteNodeRegistry.registerRouteNode(parentPath + childPath, action, method);
					    }
					}
				}
			}
		}
	}
	
	public static Object setServiceField(Class<?> cls){
		Object object = null;
		try {
			object = cls.newInstance();
		}  catch (Exception e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
		}
		 Field[] fields = cls.getDeclaredFields();
	     for(int i=0; i<fields.length;i++){  
	    	 Field field = fields[i];
	    	 field.setAccessible(true);
		     if(field.isAnnotationPresent(Autowired.class)){
		    	 Autowired ref = field.getAnnotation(Autowired.class);
		    	 Class<?> refType = field.getType();
		    	 String refValue = ref.value();
		    	 String key = getKey(refValue, refType);
		    	 
		    	 Class<?> target = annotationMap.get(key);
		    	 Object cacheObj = BeanFactory.get(key, target, false);
		    	 
		         try {
		        	field.set(object, cacheObj);
				 } catch (Exception e) {
					 log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
				 }
		     }else if(field.isAnnotationPresent(Value.class)){
		    	 Value value = field.getAnnotation(Value.class);
		    	 Class<?> valueType = field.getType();
		    	 String refValue = value.value();
		    	 try {
		    		 if(StringUtil.isNotBlank(refValue)){
		    			field.set(object, ConvertFactory.convert(valueType, refValue));
		    		 }
		    	 } catch (Exception e) {
		    		 log.error(Constant.JAVAOSC_EXCEPTION, e.getMessage());
				 }	
		     }
	     }
	     return object;
	}
	
	private static String getKey(String custKey, Class<?> cls){
		if(StringUtil.isBlank(custKey)){
			if(cls.isInterface()){
				custKey = StringUtil.formatFirstChar(cls.getSimpleName(), true);
			}else{
				Class<?>[] interCls = cls.getInterfaces();
				if(interCls!=null && interCls.length==1){ //jdk proxy key
					custKey = StringUtil.formatFirstChar(interCls[0].getSimpleName(), true);
				}else{ //cglib proxy key
					custKey = StringUtil.formatFirstChar(cls.getSimpleName(), true);
				}
			}
		}
		return custKey;
	}
	
}
