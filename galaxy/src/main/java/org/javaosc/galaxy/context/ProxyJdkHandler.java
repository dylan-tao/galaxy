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
		Object result = null;
		if(openConnection){
			ConnectionHandler.getConnection();
			if(CacheMark.getTran(method)){
				try {
					ConnectionHandler.beginTransaction();
					result = method.invoke(target, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					MethodParamHandler.getExceptionMethod(e, method, args, result);
				}finally{
					ConnectionHandler.close();
				}
			}else{
				try {
					result = method.invoke(target, args);
				} catch (Exception e) {
					MethodParamHandler.getExceptionMethod(e, method, args, result);
				}finally{
					ConnectionHandler.close();
				}
			}
			
			if(ConfigHandler.getMethodMonitor()){
				MethodParamHandler.getNormalMethod(method, args, result);
			}
			
		}else{
			result = method.invoke(target, args);
		}	
		return result;
	}
}
