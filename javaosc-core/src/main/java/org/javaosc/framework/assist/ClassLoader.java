package org.javaosc.framework.assist;

import org.javaosc.framework.constant.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ClassLoader {
	
	private static final Logger log = LoggerFactory.getLogger(ClassLoader.class);
	
	public static Class<?> load(final String name) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			log.error(ErrorMessage.CLASS_NOT_FOUND, name , e);
		}
		return null;
	}
}
