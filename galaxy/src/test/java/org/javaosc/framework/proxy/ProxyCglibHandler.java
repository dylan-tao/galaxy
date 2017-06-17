//package org.javaosc.framework.proxy;
//
//import java.lang.reflect.Method;
//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import org.javaosc.galaxy.assist.MethodParamHandler;
//import org.javaosc.galaxy.jdbc.ConnectionHandler;
///**
// * 
// * @description
// * @author Dylan Tao
// * @date 2014-09-09
// * Copyright 2014 Javaosc Team. All Rights Reserved.
// */
//
//public class ProxyCglibHandler implements MethodInterceptor {
//	
//	private Enhancer enhancer = new Enhancer();
//	
//	private Object target;
//	
//	private boolean openConnection;
//	
//	protected ProxyCglibHandler(Object target, boolean openConnection) {
//		this.target = target;
//		this.openConnection = openConnection;
//	}
//
//	@SuppressWarnings("unchecked")
//	protected <T> T  proxyInstance() {
//		enhancer.setSuperclass(target.getClass());
//        enhancer.setCallback(this);
//        return (T)enhancer.create();
//	}
//
//	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//		Object result = null;
//		if(openConnection){
//			ConnectionHandler.getConnection();
//			if(CacheMark.getTran(method)){
//				try {
//					ConnectionHandler.beginTransaction();
//					result = proxy.invokeSuper(obj, args);
//					ConnectionHandler.commit();
//				} catch (Exception e) {
//					ConnectionHandler.rollback();
//					MethodParamHandler.getExceptionMethod(e, method, args, result);
//				}finally{
//					ConnectionHandler.close();
//				}
//			}else{
//				try {
//					result = proxy.invokeSuper(obj, args);
//				} catch (Exception e) {
//					MethodParamHandler.getExceptionMethod(e, method, args, result);
//				}finally{
//					ConnectionHandler.close();
//				}
//			}
//			if(ConfigHandler.getMethodMonitor()){
//				MethodParamHandler.getNormalMethod(method, args, result);
//			}
//		}else{
//			result = proxy.invokeSuper(obj, args);
//		}	
//		return result;
//	}
//}
