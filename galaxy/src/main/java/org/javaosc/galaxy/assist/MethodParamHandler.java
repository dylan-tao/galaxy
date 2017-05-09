package org.javaosc.galaxy.assist;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.convert.ConvertFactory;
import org.javaosc.galaxy.util.JsonUtil;
import org.javaosc.galaxy.web.ActionContext;
import org.javaosc.galaxy.web.assist.FileUpload;
import org.javaosc.galaxy.web.multipart.FilePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09 Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MethodParamHandler {

	private static final Logger log = LoggerFactory.getLogger(MethodParamHandler.class);

	
	public static void getExceptionMethod(Exception e,Method method, Object[] args, Object result){
		StackTraceElement ste = e.getCause().getStackTrace()[0];
		
		log.error("{}:{} in {}", e.getCause().getClass(), e.getCause().getMessage(), ste.toString());
		if(args!=null){
			for(int i=0;i<args.length;i++){
				Object value = args[i];
				if (value == null || (value.getClass() != null) && (value.getClass().getClassLoader() == null)) {
					log.error("{}{}(...)的第{}个参数：{}",Constant.BR , method.getName() ,i+1,String.valueOf(args[i]));
				}else{
					log.error("{}{}(...)的第{}个参数：{}",Constant.BR, method.getName(), i+1,JsonUtil.toJson(args[i]));
				}
			}
		}
		
		if (result == null || (result.getClass() != null) && (result.getClass().getClassLoader() == null)) {
			log.error("{}{}(...)的返回结果：{}", Constant.BR, method.getName(), String.valueOf(result));
		}else{
			log.error("{}{}(...)的返回结果：{}", Constant.BR, method.getName(), JsonUtil.toJson(result));
		}
		
		log.error(Constant.GALAXY_EXCEPTION, e);
	}
	
	public static void getNormalMethod(Method method, Object[] args, Object result){
		
		if(args!=null){
			for(int i=0;i<args.length;i++){
				Object value = args[i];
				if (value == null || (value.getClass() != null) && (value.getClass().getClassLoader() == null)) {
					log.info("{}{}(...)的第{}个参数：{}",Constant.BR ,method.getName() ,i+1,String.valueOf(args[i]));
				}else{
					log.info("{}{}(...)的第{}个参数：{}",Constant.BR ,method.getName(), i+1,JsonUtil.toJson(args[i]));
				}
			}
		}
		
		if (result == null || (result.getClass() != null) && (result.getClass().getClassLoader() == null)) {
			log.info("{}{}(...)的返回结果：{}", Constant.BR, method.getName(), String.valueOf(result));
		}else{
			log.info("{}{}(...)的返回结果：{}", Constant.BR, method.getName(), JsonUtil.toJson(result));
		}
		
	}

	@SuppressWarnings("unchecked")
	public static Object[] getParamValue(Method m, Class<?>[] prmTypes, String[] paramNames) {
		Object[] obj = new Object[prmTypes.length];
		Map<String, Object> dataMap = ActionContext.getContext().getDataMap();

		for (int j = 0; j < prmTypes.length; j++) {

			Class<?> prmType = prmTypes[j];

			if (ClassHandler.isJavaClass(prmType)) {
				Object objValue = dataMap.get(paramNames[j]);
				if (prmType.isPrimitive()) {
					obj[j] = ConvertFactory.convert(prmType, objValue);
				} else if (prmType == String.class || ClassHandler.isWrapClass(prmType)) {
					obj[j] = ConvertFactory.convert(prmType, objValue);
				} else if (prmType.isArray()) {
					obj[j] = ConvertFactory.convert(prmType, objValue);
				} else if (prmType == HttpServletRequest.class) {
					obj[j] = ActionContext.getContext().getRequest();
				} else if (prmType == HttpServletResponse.class) {
					obj[j] = ActionContext.getContext().getResponse();
				} else {
					log.error("the data type({}) of the parameter is not supported ! you can impl convert interface(org.javaosc.framework.convert.Convert),custom your convert~", prmType.getName());
				}
			} else {
				if(prmType == FileUpload.class){
					Object objValue = dataMap.get(Constant.FILE_ARRAY);
					if(objValue!=null){
						List<FilePart> fileArray = (List<FilePart>)objValue;
						if(fileArray!=null && fileArray.size()>0){
							FileUpload f = new FileUpload();
							f.setFilePart(fileArray.get(0));
							obj[j] = f;
						}
					}
				}else if(prmType == FileUpload[].class){
					Object objValue = dataMap.get(Constant.FILE_ARRAY);
					if(objValue!=null){
						List<FilePart> fileArray = (List<FilePart>)objValue;
						if(fileArray!=null && fileArray.size()>0){
							FileUpload[] fs = new FileUpload[fileArray.size()];
							for(int q=0;q<fileArray.size();q++){
								FileUpload f = new FileUpload();
								f.setFilePart(fileArray.get(q));
								fs[q] = f;
							}
							obj[j] = fs;
						}
					}
				}else{
					try {
						if (dataMap.size() > 0) {
							obj[j] = PropertyConvert.convertMapToEntity(dataMap, prmType);
						} else {
							obj[j] = null;
						}
					} catch (Exception e) {
						log.error(Constant.GALAXY_EXCEPTION, e);
					} 
				}
			}
		}
		return obj;
	}

	public static String[] getParamName(Method method) {
		try {
			int size = method.getParameterTypes().length;
			if (size == 0) {
				return new String[0];
			}else{
				Parameter[] params = method.getParameters();
				String[] methodNames = new String[params.length];
				for (int i=0;i<params.length;i++) {  
					System.out.println(params[i].getName());
					methodNames[i] = params[i].getName();
		        }
				return methodNames;
			}
		} catch (Throwable e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
			return null;
		}
	}

}
