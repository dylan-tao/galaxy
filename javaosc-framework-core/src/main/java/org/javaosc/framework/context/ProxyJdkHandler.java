package org.javaosc.framework.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

public class ProxyJdkHandler implements InvocationHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ProxyJdkHandler.class);
	
	private Class<?> target;
	
	private boolean isTransaction;

	protected ProxyJdkHandler(Class<?> target, boolean isTransaction) {
		this.target = target;
		this.isTransaction = isTransaction;
	}

	@SuppressWarnings("unchecked")
	protected <T> T proxyInstance() {
	    return (T)Proxy.newProxyInstance(target.getClassLoader(), target.getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
					returnObj = method.invoke(proxy, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					log.error(Constant.JAVAOSC_EXCEPTION, e);;
				}
			}else{
				returnObj = method.invoke(proxy, args);
			}
			ConnectionHandler.close();
		}else{
			returnObj = method.invoke(proxy, args);
		}	
		return returnObj;
	}
}
