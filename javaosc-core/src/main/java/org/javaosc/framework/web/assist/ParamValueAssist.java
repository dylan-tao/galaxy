package org.javaosc.framework.web.assist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaosc.framework.web.ActionContext;
import org.javaosc.framework.web.util.FileUpload;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public abstract class ParamValueAssist {
	
	private static Log log = LogFactory.getLog(ParamValueAssist.class);
	
	@SuppressWarnings("unchecked")
	public static Object[] getPrmValue(Method m, Class<?>[] prmTypes,List<String> paramNames){
		Object[] obj = new Object[prmTypes.length];
		if(paramNames == null){
			paramNames = MethodPrmName.getParamNames(m);
		}
		Map<String, Object> dataMap =ActionContext.getContext().getRequest().getParameterMap();
		boolean uploadPrmLoad = false;
		
		for(int i = 0;i < prmTypes.length;i++){
			
			Class<?> prmType = prmTypes[i];
			
			if(isJavaClass(prmType)){
				Object objValue = dataMap.get(paramNames.get(i));
				if(prmType.isPrimitive()){
					obj[i] = ConvertUtils.convert(objValue, prmType);
				}else if(prmType == String.class || isWrapClass(prmType)){
					obj[i] = objValue == null ? objValue : ConvertUtils.convert(objValue, prmType);
				}else if(prmType.isArray()){
					obj[i] = objValue == null ? objValue : ConvertUtils.convert((String[])objValue, prmType);
				}else if(prmType == HttpServletRequest.class){
					obj[i] = ActionContext.getContext().getRequest();
				}else if(prmType == HttpServletResponse.class){ 
					obj[i] = ActionContext.getContext().getResponse();
				}else{
					log.error("[errorCode:1113] the data type("+prmType.getName()+") of the parameter is not supported !");
				}
			}else if(prmType == FileUpload.class && paramNames.size()>1){
				if(!uploadPrmLoad){
					PutPrmAssist prmParse = new PutPrmAssist(ActionContext.getContext().getRequest());
					Map<String, Object> map = prmParse.getData();
					if(map!=null && map.size() > 0){
						dataMap.putAll(map);
						i=0;
						uploadPrmLoad = true;
					}else{
						obj[i] = null;
					}
				}else{
					obj[i] = null;
				}
				
			}else{
				try {
					if(dataMap.size()>0){
						Object bean = prmType.newInstance();
						BeanUtils.populate(bean, dataMap);
						obj[i] = prmType.cast(bean);
					}else{
						obj[i] = null;
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
