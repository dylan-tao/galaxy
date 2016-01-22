package org.javaosc.framework.assist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassLoader {
	
	private static final Log log = LogFactory.getLog(ClassLoader.class);
	
	public static Class<?> load(final String name) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			log.error("[errorCode:1115] please check this class("+name+") files can not be found and see the following Caused by: !",e);
		}
		return null;
	}
}
