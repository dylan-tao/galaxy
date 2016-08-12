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

import org.javaosc.framework.constant.Configuration;
import org.javaosc.framework.constant.Constant;
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
	
	public List<String> getClassName(String packageName) {
		List<String> classNameList = new ArrayList<String>();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(packageName.replace(Constant.DOT, Constant.LINE));
			if(url == null){
				log.error("class package scan directory [{}] not found! please check setting: {}=? in the {}" , packageName, Configuration.SCANER_PACKAGE_KEY, ConfigurationHandler.javaoscConfig);
			}else{
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					File[] files = new File(url.toURI()).listFiles();
					files = files ==null? new File[0]:files;
					for (File f : files) {
						scanFile(packageName, f, classNameList);
					}
				} else if ("jar".equals(protocol)) {
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					scanJar(jar, packageName, classNameList);
				}
				log.info("class package scan is completed.");
			}
		} catch (URISyntaxException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		} catch (IOException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}	
		return classNameList;
	}

	private void scanFile(String packageName, File file, List<String> classNameList) {
		String fileName = file.getName();
		if (file.isFile() && fileName.endsWith(Constant.SUFFIX_CLASS) && isHasClassKeyword(fileName)) {
			fileName = fileName.substring(0, fileName.length() - 6);
			classNameList.add(new StringBuffer(packageName).append(Constant.DOT).append(fileName).toString());
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			files = files ==null? new File[0]:files;
			for (File f : files) {
				scanFile(new StringBuffer(packageName).append(Constant.DOT).append(fileName).toString(), f, classNameList);
			}
		}
	}

	private void scanJar(JarFile jarFile, String packageName, List<String> classNameList) {
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String fileName = entry.getName().replace(Constant.LINE, Constant.DOT);
			if (fileName.startsWith(packageName) && fileName.endsWith(Constant.SUFFIX_CLASS) && isHasClassKeyword(fileName)) {
				classNameList.add(fileName.substring(0, fileName.length() - 6));
			}
		}
	}
	
	private boolean isHasClassKeyword(String fileName){
		boolean flag = false;
		String[] keywords = ConfigurationHandler.getScanClassNameKeyword();
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
