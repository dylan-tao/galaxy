package org.javaosc.galaxy.assist;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
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

import net.sf.cglib.asm.$ClassReader;
import net.sf.cglib.asm.$ClassVisitor;
import net.sf.cglib.asm.$Label;
import net.sf.cglib.asm.$MethodVisitor;
import net.sf.cglib.asm.$Opcodes;
import net.sf.cglib.asm.$Type;

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

	public static String[] getParamNameByJdk8(Method method) {
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
	
	public static String[] getParamNameByAsm(Class<?> clazz, final Method method) {  
        final Class<?>[] parameterTypes = method.getParameterTypes();  
        if (parameterTypes == null || parameterTypes.length == 0) {  
            return null;  
        }  
        final $Type[] types = new $Type[parameterTypes.length];  
        for (int i = 0; i < parameterTypes.length; i++) {  
            types[i] = $Type.getType(parameterTypes[i]);  
        }  
        final String[] parameterNames = new String[parameterTypes.length];  
  
        String className = clazz.getName();  
        int lastDotIndex = className.lastIndexOf(".");  
        className = className.substring(lastDotIndex + 1) + ".class";  
        InputStream is = clazz.getResourceAsStream(className);  
        try {  
        	$ClassReader classReader = new $ClassReader(is);  
            classReader.accept(new $ClassVisitor($Opcodes.ASM5) {  
                @Override  
                public $MethodVisitor visitMethod(int access, String name,  
                        String desc, String signature, String[] exceptions) {  
                    // 只处理指定的方法  
                    $Type[] argumentTypes = $Type.getArgumentTypes(desc);  
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {  
                        return super.visitMethod(access, name, desc, signature,  
                                exceptions);  
                    }  
                    return new $MethodVisitor($Opcodes.ASM5) {  
                        @Override  
                        public void visitLocalVariable(String name, String desc,  
                                String signature, $Label start,  
                                $Label end, int index) {  
                            // 非静态成员方法的第一个参数是this  
                            if (Modifier.isStatic(method.getModifiers())) {  
                                parameterNames[index] = name;  
                            } else if (index > 0) {  
                                parameterNames[index - 1] = name;  
                            }  
                        }  
                    };  
                }  
            }, 0);  
        } catch (IOException e) {  
        } finally {  
            try {  
                if (is != null) {  
                    is.close();  
                }  
            } catch (Exception e2) {  
            }  
        }  
        return parameterNames;  
    }  

}
