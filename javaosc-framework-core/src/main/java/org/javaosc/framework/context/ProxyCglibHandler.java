package org.javaosc.framework.context;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.jdbc.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class ProxyCglibHandler implements MethodInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(ProxyCglibHandler.class);
	
	private Enhancer enhancer = new Enhancer();
	
	private Class<?> cls;
	
	private boolean isTransaction;
	
	protected ProxyCglibHandler(Class<?> cls, boolean isTransaction) {
		this.cls = cls;
		this.isTransaction = isTransaction;
	}

	@SuppressWarnings("unchecked")
	protected <T> T  proxyInstance() {
		Class<?>[] interfaceArray = this.cls.getInterfaces();
		if(interfaceArray != null && interfaceArray.length>0){
			enhancer.setInterfaces(interfaceArray);
		}else{
			enhancer.setSuperclass(this.cls);
		}
        enhancer.setCallback(this);  
        return (T)enhancer.create();  
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		boolean isHasTx = false;
		Object returnObj = null;
		if(isTransaction && ConfigurationHandler.getMethodKeyword() != null){
			for(String keyword:ConfigurationHandler.getMethodKeyword()){
				if(method.getName().startsWith(keyword)){
					isHasTx = true;
					break;
				}
			}
			ConnectionHandler.getConnection();
			if(isHasTx){
				try {
					ConnectionHandler.beginTransaction();
					returnObj = proxy.invokeSuper(obj, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					log.error(Constant.JAVAOSC_EXCEPTION, e);
				}
			}else{
				returnObj = proxy.invokeSuper(obj, args);
			}
			ConnectionHandler.close();
		}else{
			returnObj = proxy.invokeSuper(obj, args);
		}	
		return returnObj;
	}
}
