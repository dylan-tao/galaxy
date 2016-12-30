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
		
		if(ConfigHandler.getStartedStatus()){
			log.info("    ______          __                         ");
			log.info("  .' ___  |        [  |            {}", "v1.0.0.RELEASE");
			log.info(" / .'   \\_|  ,--.   | |  ,--.   _   __   _   __ ");
			log.info(" | |   ____ `'_\\ :  | | `'_\\ : [ \\ [  ] [ \\ [  ]");
			log.info(" \\ `.___]  |// | |, | | // | |, > '  <   \\ '/ /    Startup in {} ms", initTime);
			log.info("  `._____.' \\'-;__/[___]\\'-;__/[__]`\\_] [ \\_:/  ");
			log.info("                                         \\_.'  ");
			log.info(" Thanks ~ Good Luck! | Community: www.javaosc.com  ");
		}else{
			log.info("====== Galaxy framework startup in {} ms ======", initTime);
		}
		
		ConfigHandler.clear();
		
		System.gc();
	}
}
