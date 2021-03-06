package org.javaosc.galaxy.context;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.javaosc.galaxy.assist.ClassHandler;
import org.javaosc.galaxy.constant.Configuration;
import org.javaosc.galaxy.constant.Constant;
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
	
	public void load() {
		String packageName = ConfigHandler.getScanPackage();
		if(packageName!=null){
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				URL url = loader.getResource(packageName.replace(Constant.DOT, Constant.LINE));
				if(url == null){
					log.error("class package scan directory [{}] not found! please check setting: {}=? in the {}" , packageName, Configuration.SCANER_PACKAGE_KEY, ConfigHandler.galaxyConfig);
				}else{
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						File[] files = new File(url.toURI()).listFiles();
						files = files ==null? new File[0]:files;
						for (File f : files) {
							scanFile(packageName, f);
						}
					} else if ("jar".equals(protocol)) {
						JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
						scanJar(jar, packageName);
					}
					log.info("class package and annotation scan is completed.");
				}
			} catch (URISyntaxException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			} catch (IOException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			}	
		}
	}

	private void scanFile(String packageName, File file) {
		String fileName = file.getName();
		if (file.isFile() && fileName.endsWith(Constant.SUFFIX_CLASS)) {
			fileName = fileName.substring(0, fileName.length() - 6);
			Class<?> loadClass = ClassHandler.load(new StringBuffer(packageName).append(Constant.DOT).append(fileName).toString());
			ScanAnnotation.putAnnotation(loadClass);
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			files = files ==null? new File[0]:files;
			for (File f : files) {
				scanFile(new StringBuffer(packageName).append(Constant.DOT).append(fileName).toString(), f);
			}
		}
	}

	private void scanJar(JarFile jarFile, String packageName) {
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String fileName = entry.getName().replace(Constant.LINE, Constant.DOT);
			if (fileName.startsWith(packageName) && fileName.endsWith(Constant.SUFFIX_CLASS)) {
				Class<?> loadClass = ClassHandler.load(fileName.substring(0, fileName.length() - 6));
				ScanAnnotation.putAnnotation(loadClass);
			}
		}
	}
}
