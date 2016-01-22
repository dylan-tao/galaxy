package org.javaosc.framework.web;

import java.beans.Introspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.context.AnnotationScaner;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.Configuration;
import org.javaosc.framework.orm.ConnectionHandler;
import org.javaosc.framework.web.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ContextListener implements ServletContextListener {
	
	private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent contextEvent) {
		ActionContext.destroy();
		Configuration.clear();
		BeanFactory.clear();
		RouteNodeRegistry.clear();
		ConnectionHandler.destroy();
		
		Introspector.flushCaches();
		
		log.info("====== uufast Framework flushing cache ======");
	}

	public void contextInitialized(ServletContextEvent contextEvent) {
		long initTime = System.currentTimeMillis();
		ServletContext sc = contextEvent.getServletContext();
		
		String log4jFile =sc.getInitParameter("log4jConfigLocation");
		if(log4jFile != null){
		   PropertyConfigurator.configure(PathUtil.getClassPath() + log4jFile);
		}
		
		Configuration.setConfigFileName(sc.getInitParameter(ProperConstant.CONFIG_FILE_NAME));
		Configuration.load();
		
		new AnnotationScaner().load();
		
		ConnectionHandler.init();
		
		BeanFactory.initKeywords();
		
		initTime = System.currentTimeMillis() - initTime;
		
		log.info("====== uufast Framework startup in " + initTime + " ms ======");
		System.gc();
	}


}
