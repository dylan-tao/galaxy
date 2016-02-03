package org.javaosc.framework.context;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.javaosc.framework.ddx.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class ProxyHandler implements MethodInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(ProxyHandler.class);
	
	private Class<?> cls;
	
	private String[] keyword;
	
	private boolean isTransaction;

	protected ProxyHandler(Class<?> cls, String[] keyword, boolean isTransaction) {
		this.cls = cls;
		this.keyword = keyword;
		this.isTransaction = isTransaction;
	}

	protected Object proxyInstance() {
		Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(this.cls);  
        enhancer.setCallback(this);  
        return enhancer.create();  
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		boolean isHasTx = false;
		Object returnObj = null;
		if(isTransaction && keyword != null){
			for(int u = 0; u < keyword.length; u++){
				if(method.getName().startsWith(keyword[u])){
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
					e.printStackTrace();
				}
			}else{
				proxy.invokeSuper(obj, args);
			}
			ConnectionHandler.close();
		}else{
			returnObj = proxy.invokeSuper(obj, args);
		}	
		return returnObj;
	}
}
