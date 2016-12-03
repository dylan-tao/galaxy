package org.javaosc.galaxy.web;

import java.beans.Introspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.javaosc.galaxy.constant.Configuration;
import org.javaosc.galaxy.context.BeanFactory;
import org.javaosc.galaxy.context.ConfigExtHandler;
import org.javaosc.galaxy.context.ConfigHandler;
import org.javaosc.galaxy.context.ScanAnnotation;
import org.javaosc.galaxy.context.ScanPackage;
import org.javaosc.galaxy.jdbc.ConnectionHandler;
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
		
		ConfigHandler.clear();
		
		ConfigExtHandler.clear();
		
		BeanFactory.clear();
		
		Introspector.flushCaches();
		System.gc();
		
		log.info("====== Galaxy Framework startup failed ======");
	}

	public void contextInitialized(ServletContextEvent event) {
		
		long initTime = System.currentTimeMillis();
		
		ServletContext sc = event.getServletContext();
		
		ConfigHandler.load(sc.getInitParameter(Configuration.CONFIG_FILE_NAME));
		
		ConfigExtHandler.load(sc.getInitParameter(Configuration.EXPEND_FILE_NAME));
		
		ScanPackage scan = new ScanPackage();
		scan.load();
		scan = null;
		
		ScanAnnotation.registryAnnotation();
		
		ConnectionHandler.init();
		
		log.debug("Bean factory: {}", BeanFactory.beanMap);
		
		initTime = System.currentTimeMillis() - initTime;
		log.info("================== support site ==================");
		log.info(" ");
		log.info("     Galaxy framework startup in {} ms", initTime);
		log.info(" ");
		log.info("================= www.javaosc.com ================");
		
		ConfigHandler.clear();
		
		System.gc();
	}
}
