package org.javaosc.galaxy.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import org.javaosc.galaxy.annotation.Autowired;
import org.javaosc.galaxy.annotation.Bean;
import org.javaosc.galaxy.annotation.Component;
import org.javaosc.galaxy.annotation.Dao;
import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.annotation.Prototype;
import org.javaosc.galaxy.annotation.Service;
import org.javaosc.galaxy.annotation.Value;
import org.javaosc.galaxy.assist.ClassHandler;
import org.javaosc.galaxy.assist.PropertyConvert;
import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.convert.ConvertFactory;
import org.javaosc.galaxy.jdbc.JdbcTemplate;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.javaosc.galaxy.web.RouteNodeRegistry;
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
	
	private static HashMap<String, Class<?>> annotationMap = new HashMap<String, Class<?>>();
	
	protected static void putAnnotation(Class<?> loadClass){
		
		annotationMap.put("jdbcTemplate", JdbcTemplate.class);
		
		String custKey = null;
		boolean flag = false;
		if(loadClass.isAnnotationPresent(Dao.class)){
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
		}else if(loadClass.isAnnotationPresent(Component.class)){
			Component bean = loadClass.getAnnotation(Component.class);
			custKey = bean.value();
			flag = true;
		}else if(loadClass.isAnnotationPresent(Bean.class)){
			PropertyConvert.getPropertyMap(loadClass);
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
				if(cls.isAnnotationPresent(Component.class)){
					BeanFactory.get(key, cls, false);
				}else if(cls.isAnnotationPresent(Dao.class)){
					BeanFactory.get(key, cls, ConfigHandler.getDatasouceStatus());
				}else if(cls.isAnnotationPresent(Service.class)){
					BeanFactory.get(key, cls, ConfigHandler.getDatasouceStatus());
					CacheMark.putServiceAnno(cls);
				}else if(cls.isAnnotationPresent(Mapping.class)){
					Object action = null;
					String parentPath = "";
					
					if(cls.isAnnotationPresent(Prototype.class)){
						action = cls;
					}else{
						try {
							action = setServiceField(cls, ClassHandler.newInstanceObj(cls));
						}catch (Exception e) {
							e.printStackTrace();
						}
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
	
	public static Object setServiceField(Class<?> cls,Object proxyInst){
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
		        	field.set(proxyInst, cacheObj);
				 } catch (Exception e) {
					 log.error(Constant.GALAXY_EXCEPTION, e);
				 }
		     }else if(field.isAnnotationPresent(Value.class)){
		    	 Value value = field.getAnnotation(Value.class);
		    	 Class<?> valueType = field.getType();
		    	 String refValue = value.value();
		    	 try {
		    		 if(!GalaxyUtil.isEmpty(refValue)){
		    			refValue = ConfigExtHandler.getValue(refValue);
		    			if(!GalaxyUtil.isEmpty(refValue)){
		    				field.set(proxyInst, ConvertFactory.convert(valueType, refValue));
		    			}
		    		 }
		    	 } catch (Exception e) {
		    		 log.error(Constant.GALAXY_EXCEPTION, e);
				 }	
		     }
	     }
	     return proxyInst;
	}
	
	private static String getKey(String custKey, Class<?> cls){
		if(GalaxyUtil.isEmpty(custKey)){
			if(cls.isInterface()){
				custKey = GalaxyUtil.formatFirstChar(cls.getSimpleName(), true);
			}else{
				Class<?>[] interCls = cls.getInterfaces();
				if(interCls!=null && interCls.length==1){ //jdk proxy key
					custKey = GalaxyUtil.formatFirstChar(interCls[0].getSimpleName(), true);
				}else{ //cglib proxy key
					custKey = GalaxyUtil.formatFirstChar(cls.getSimpleName(), true);
				}
			}
		}
		return custKey;
	}
	
}
