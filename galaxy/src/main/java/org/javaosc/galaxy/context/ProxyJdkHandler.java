package org.javaosc.galaxy.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.javaosc.galaxy.assist.MethodParamHandler;
import org.javaosc.galaxy.jdbc.ConnectionHandler;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class ProxyJdkHandler implements InvocationHandler {
	
//	private static final Logger log = LoggerFactory.getLogger(ProxyJdkHandler.class);
	
	private Object target;
	
	private boolean openConnection;

	protected ProxyJdkHandler(Object target, boolean openConnection) {
		this.target = target;
		this.openConnection = openConnection;
	}

	@SuppressWarnings("unchecked")
	protected <T> T proxyInstance() {
	    return (T)Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object returnObj = null;
		if(openConnection){
			ConnectionHandler.getConnection();
			if(CacheMark.getTran(method)){
				try {
					ConnectionHandler.beginTransaction();
					returnObj = method.invoke(target, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					MethodParamHandler.getMethodParam(e, method, args);
				}
			}else{
				try {
					returnObj = method.invoke(target, args);
				} catch (Exception e) {
					MethodParamHandler.getMethodParam(e, method, args);
				}
			}
			ConnectionHandler.close();
		}else{
			returnObj = method.invoke(target, args);
		}	
		return returnObj;
	}
}
