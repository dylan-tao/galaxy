package org.javaosc.framework.web.assist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.javaosc.framework.web.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public abstract class ParamValueAssist {
	
	private static final Logger log = LoggerFactory.getLogger(ParamValueAssist.class);
	
	public static Object[] getPrmValue(Method m, Class<?>[] prmTypes,List<String> paramNames){
		Object[] obj = new Object[prmTypes.length];
		Map<String, String[]> dataMap = ActionContext.getContext().getRequest().getParameterMap();
		
		for(int j = 0;j < prmTypes.length;j++){
			
			Class<?> prmType = prmTypes[j];
			
			if(isJavaClass(prmType)){
				Object objValue = dataMap.get(paramNames.get(j));
				if(prmType.isPrimitive()){
					obj[j] = ConvertUtils.convert(objValue, prmType);
				}else if(prmType == String.class || isWrapClass(prmType)){
					obj[j] = objValue == null ? objValue : ConvertUtils.convert(objValue, prmType);
				}else if(prmType.isArray()){
					obj[j] = objValue == null ? objValue : ConvertUtils.convert((String[])objValue, prmType);
				}else if(prmType == HttpServletRequest.class){
					obj[j] = ActionContext.getContext().getRequest();
				}else if(prmType == HttpServletResponse.class){ 
					obj[j] = ActionContext.getContext().getResponse();
				}else{
					log.error("the data type({}) of the parameter is not supported !", prmType.getName());
				}
			}else{
				try {
					if(dataMap.size()>0){
						Object bean = prmType.newInstance();
						BeanUtils.populate(bean, dataMap);
						obj[j] = prmType.cast(bean);
					}else{
						obj[j] = null;
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}	
			}
		}
		return obj;
	}
	
	private static boolean isJavaClass(Class<?> cls){
		return cls != null && cls.getClassLoader() == null;  
	}
	
	private static boolean isWrapClass(Class<?> clz) {     
        try {     
           return ((Class<?>)clz.getField("TYPE").get(null)).isPrimitive();    
        } catch (Exception e) {     
           return false;     
        }     
    }     

}
