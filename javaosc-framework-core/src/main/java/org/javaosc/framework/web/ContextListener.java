package org.javaosc.framework.web;

import java.beans.Introspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.javaosc.framework.constant.Configuration;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.ConfigurationHandler;
import org.javaosc.framework.context.ScanAnnotation;
import org.javaosc.framework.context.ScanPackage;
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
public class ContextListener implements ServletContextListener {
	
	private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		
		ActionContext.destroy();
		
		RouteNodeRegistry.clear();
		
		ConnectionHandler.destroy();
		
		ConfigurationHandler.clear();
		
		BeanFactory.clear();
		
		Introspector.flushCaches();
		System.gc();
		
		log.info("====== Javaosc Framework startup failed ======");
	}

	public void contextInitialized(ServletContextEvent event) {
		
		long initTime = System.currentTimeMillis();
		
		ServletContext sc = event.getServletContext();
		
		ConfigurationHandler.load(sc.getInitParameter(Configuration.CONFIG_FILE_NAME));
		
		ScanPackage scan = new ScanPackage();
		scan.load();
		scan = null;
		
		ScanAnnotation.registryAnnotation();
		
		ConnectionHandler.init();
		
		log.debug("Bean factory: {}", BeanFactory.beanMap);
		
		initTime = System.currentTimeMillis() - initTime;
		
		log.info("====== Javaosc Framework startup in {} ms ======", initTime);
		
		ConfigurationHandler.clear();
		
		System.gc();
	}
}
