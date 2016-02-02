package org.javaosc.framework.web;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.context.AnnotationScaner;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.Configuration;
import org.javaosc.framework.ddx.ConnectionHandler;
import org.javaosc.framework.web.util.PathUtil;
import org.javaosc.framework.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

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
		Configuration.clear();
		BeanFactory.clear();
		RouteNodeRegistry.clear();
		ConnectionHandler.destroy();
		
		Introspector.flushCaches();
		
		log.info("====== Javaosc Framework flushing cache ======");
	}

	public void contextInitialized(ServletContextEvent event) {
		long initTime = System.currentTimeMillis();
		
		ServletContext sc = event.getServletContext();
		
//		String logbackFile =sc.getInitParameter("logBackConfigLocation");
//		if(StringUtil.isNotBlank(logbackFile)){
//			Logba.load(PathUtil.getClassPath() + logbackFile);
//		}
		
		Configuration.setConfigFileName(sc.getInitParameter(ProperConstant.CONFIG_FILE_NAME));
		Configuration.load();
		
		new AnnotationScaner().load();
		
		ConnectionHandler.init();
		
		BeanFactory.initKeywords();
		
		initTime = System.currentTimeMillis() - initTime;
		
		log.info("====== Javaosc Framework startup in {} ms ======", initTime);
		System.gc();
	}

	private static void load (String logbackFileLocation) throws IOException, JoranException{  
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();  
          
        File externalConfigFile = new File(logbackFileLocation);  
        if(!externalConfigFile.exists()){  
        	log.error(arg0);
            throw new IOException("Logback External Config File Parameter does not reference a file that exists");  
        }else{  
            if(!externalConfigFile.isFile()){  
                throw new IOException("Logback External Config File Parameter exists, but does not reference a file");  
            }else{  
                if(!externalConfigFile.canRead()){  
                    throw new IOException("Logback External Config File exists and is a file, but cannot be read.");  
                }else{  
                    JoranConfigurator configurator = new JoranConfigurator();  
                    configurator.setContext(lc);  
                    lc.reset();  
                    configurator.doConfigure(logbackFileLocation);  
                    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);  
                }  
            }     
        }  
    }  

}
