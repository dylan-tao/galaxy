package org.javaosc.framework.context;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.ProperConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ScanPackage {
	
	private static final Logger log = LoggerFactory.getLogger(ScanPackage.class);
	
	private String[] keywords = null;
	
	public List<String> getClassName(String packageName) {
		List<String> className = new ArrayList<String>();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(packageName.replace(Constant.DOT, Constant.LINE));
			if(url == null){
				log.error("package[ {} ] can not found! please check setting : {}" , packageName, ProperConstant.SCANER_PACKAGE_KEY);
			}else{
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					File[] files = new File(url.toURI()).listFiles();
					for (File f : files) {
						scanFile(packageName, f, className);
					}
					log.info("file scan has been completed");
				} else if ("jar".equals(protocol)) {
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					scanJar(jar, packageName, className);
					log.info("jar scan has been completed");
				}
			}
		} catch (URISyntaxException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		} catch (IOException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}	
		return className;
	}

	private void scanFile(String packageName, File file, List<String> className) {
		String fileName = file.getName();
		if (file.isFile() && fileName.endsWith(Constant.SUFFIX_CLASS) && isHasClassKeyword(fileName)) {
			fileName = fileName.substring(0, fileName.length() - 6);
			className.add(packageName + Constant.DOT + fileName);
		} else if (file.isDirectory()) {
			packageName = packageName + Constant.DOT +  fileName;
			for (File f : file.listFiles()) {
				scanFile(packageName, f, className);
			}
		}
	}

	private void scanJar(JarFile jarFile, String packageName, List<String> className) {
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String fileName = entry.getName().replace(Constant.LINE, Constant.DOT);
			if (fileName.startsWith(packageName) && fileName.endsWith(Constant.SUFFIX_CLASS) && isHasClassKeyword(fileName)) {
				className.add(fileName.substring(0, fileName.length() - 6));
			}
		}
	}
	
	private boolean isHasClassKeyword(String fileName){
		boolean flag = false;
		if(keywords == null){
			String keyword = Configuration.getValue(ProperConstant.CLASS_KEYWORD_KEY, ProperConstant.CLASS_KEYWORD_VALUE);
			if(keyword.indexOf(Constant.COMMA)!=-1){
				keywords = keyword.split(Constant.COMMA);
			}else{
				keywords = new String[]{keyword};
			}
		}
		fileName = fileName.toLowerCase();
		for(int u = 0; u < keywords.length; u++){
			if(fileName.indexOf(keywords[u]) > 0){
				flag = true;
				break;
			}
		}
		return flag;
	}
}
