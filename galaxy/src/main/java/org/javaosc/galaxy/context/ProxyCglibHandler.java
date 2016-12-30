package org.javaosc.galaxy.context;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.javaosc.galaxy.assist.MethodParamHandler;
import org.javaosc.galaxy.jdbc.ConnectionHandler;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class ProxyCglibHandler implements MethodInterceptor {
	
//	private static final Logger log = LoggerFactory.getLogger(ProxyCglibHandler.class);
	
	private Enhancer enhancer = new Enhancer();
	
	private Object target;
	
	private boolean openConnection;
	
	protected ProxyCglibHandler(Object target, boolean openConnection) {
		this.target = target;
		this.openConnection = openConnection;
	}

	@SuppressWarnings("unchecked")
	protected <T> T  proxyInstance() {
		enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (T)enhancer.create();
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object returnObj = null;
		if(openConnection){
			ConnectionHandler.getConnection();
			if(CacheMark.getTran(method)){
				try {
					ConnectionHandler.beginTransaction();
					returnObj = proxy.invokeSuper(obj, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					MethodParamHandler.getMethodParam(e, method, args);
				}
			}else{
				try {
					returnObj = proxy.invokeSuper(obj, args);
				} catch (Exception e) {
					MethodParamHandler.getMethodParam(e, method, args);
				}
			}
			ConnectionHandler.close();
		}else{
			returnObj = proxy.invokeSuper(obj, args);
		}	
		return returnObj;
	}
}