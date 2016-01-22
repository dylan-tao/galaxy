package org.javaosc.framework.web.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.StorageRule;
import org.javaosc.framework.constant.FileConfig;
import org.javaosc.framework.web.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class FileUpload {
	
	private static final Logger log = LoggerFactory.getLogger(FileUpload.class);
	
	private String fileDir;
	
	private int maxSize;

	private String[] contentType;
	
	private StorageRule storageRule;

	/**
	 * @param fileDir 上传的基础目录[ps:/WEB-INF/upload]
	 * @param maxSize 文件大小限制[kb]
	 * @param storageRule 存储规则
	 * @param contentType 允许的文件类型
	 */
	public FileUpload(String fileDir, int maxSize, StorageRule storageRule, String... contentType) {
		this.fileDir = fileDir;
		this.maxSize = maxSize;
		this.contentType = contentType;
		this.storageRule = storageRule;
	}
	
	public FileConfig upload(){
		
		HttpServletRequest request = ActionContext.getContext().getRequest();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
	    factory.setSizeThreshold(4096);
	    File tempFile = new File("/WEB-INF/file_temp");
	    if(!tempFile.exists()) tempFile.mkdirs();
	    factory.setRepository(tempFile);
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setSizeMax(maxSize);
	    
	    List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		
	    String rulePath = "";
	    StringBuffer lastFilePath = new StringBuffer(fileDir).append(Constant.LINE);
	    Iterator<FileItem> i = items.iterator();
	    FileConfig fileConfig = new FileConfig();
	    
	    while (i.hasNext()) {
	       FileItem item = (FileItem) i.next();
	       String fileName = item.getName();
	        if (!item.isFormField() && StringUtil.isNotBlank(fileName)) {
	    		String fileContentType = item.getContentType();
	    		fileConfig.setFileName(fileName);
	    		fileConfig.setContentType(fileContentType);
	    	
	        	if(validateType(fileContentType)){
	        		
	        		if(storageRule!=null && StringUtil.isBlank(rulePath)){
	        			Date date = new Date();
	        			if(storageRule.getValue()==0){ //year
	        				rulePath = new SimpleDateFormat("yyyy\\").format(date);
	        			}else if(storageRule.getValue()==1){ //month
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\").format(date);
	        			}else if(storageRule.getValue()==2){ //day
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\dd\\").format(date);
	        			}else if(storageRule.getValue()==3){ //hour
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\dd\\HH\\").format(date);
	        			}
	        			lastFilePath.append(rulePath.replace("\\", Constant.LINE));
	        		}
	        		
	        		String originalFilename = item.getFieldName();
		    		String newFileName = rename(fileName);
		    		long fileSize = item.getSize();
		    		lastFilePath.append(newFileName);
		    		fileConfig.setCreateTime(System.currentTimeMillis());
		    		fileConfig.setFilePath(lastFilePath.toString());
		    		fileConfig.setFileSize(fileSize);
		    		fileConfig.setNewFileName(newFileName);
		    		
		            log.debug("文件名称: " + fileName);  
		            log.debug("文件原名: " + originalFilename);  
		            log.debug("文件新名: " + newFileName);
		    		log.debug("文件大小: " + fileSize); 
		    		log.debug("文件类型: " + fileContentType); 
		    		
		            // \\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\
		            String filePath = request.getSession().getServletContext().getRealPath(fileDir) + rulePath; 
		            try {
		    			FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filePath, newFileName));
		    			fileConfig.setCode(1);
		    		} catch (IOException e) {
		    			fileConfig.setCode(0);
		    			log.error(e);
		    		}
	        	}else{
	        		fileConfig.setCode(-1);
	        	}
	        	break;
	        }
	     }
        return fileConfig;
	}
	
	public List<FileConfig> MultiUpload(){
		
		HttpServletRequest request = ActionContext.getContext().getRequest();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
	    factory.setSizeThreshold(4096);
	    File tempFile = new File("/WEB-INF/temp");
	    if(!tempFile.exists()) tempFile.mkdirs();
	    factory.setRepository(tempFile);
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setSizeMax(maxSize);
	    
	    List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		
	    String rulePath = "";
	    StringBuffer lastFilePath = new StringBuffer(fileDir).append(Constant.LINE);
	    Iterator<FileItem> i = items.iterator();
	    List<FileConfig> lastPathList = new ArrayList<FileConfig>();
	    
	    while (i.hasNext()) {
	       FileItem item = (FileItem) i.next();
	       String fileName = item.getName();
	        if (!item.isFormField() && StringUtil.isNotBlank(fileName)) {
	    		String fileContentType = item.getContentType();
	    		
	    		FileConfig fileConfig = new FileConfig();
	    		fileConfig.setFileName(fileName);
	    		fileConfig.setContentType(fileContentType);
	    	
	        	if(validateType(fileContentType)){
	        		if(storageRule!=null && StringUtil.isBlank(rulePath)){
	        			Date date = new Date();
	        			if(storageRule.getValue()==0){ //year
	        				rulePath = new SimpleDateFormat("yyyy\\").format(date);
	        			}else if(storageRule.getValue()==1){ //month
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\").format(date);
	        			}else if(storageRule.getValue()==2){ //day
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\dd\\").format(date);
	        			}else if(storageRule.getValue()==3){ //hour
	        				rulePath = new SimpleDateFormat("yyyy\\MM\\dd\\HH\\").format(date);
	        			}
	        			lastFilePath.append(rulePath.replace("\\", Constant.LINE));
	        		}
	        		
	        		String originalFilename = item.getFieldName();
		    		String newFileName = rename(fileName);
		    		long fileSize = item.getSize();
		    		lastFilePath.append(newFileName);
		    		fileConfig.setCreateTime(System.currentTimeMillis());
		    		fileConfig.setFilePath(lastFilePath.toString());
		    		fileConfig.setFileSize(fileSize);
		    		fileConfig.setNewFileName(newFileName);
		    		
		            log.debug("文件名称: " + fileName);  
		            log.debug("文件原名: " + originalFilename);  
		            log.debug("文件新名: " + newFileName);
		    		log.debug("文件大小: " + fileSize); 
		    		log.debug("文件类型: " + fileContentType); 
		    		
		            // \\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\
		            String filePath = request.getSession().getServletContext().getRealPath(fileDir) + rulePath; 
		            try {
		    			FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filePath, newFileName));
		    			fileConfig.setCode(1);
		    		} catch (IOException e) {
		    			log.error(e);
		    			fileConfig.setCode(0);
		    		}
	        	}else{
	        		fileConfig.setCode(-1);
	        	}
	        	
	        	lastPathList.add(fileConfig);
	        }
	     }
        return lastPathList;
	}
	
	private boolean validateType(String fileContentType){
		boolean flag = false;
		for(int u = 0; u < contentType.length; u++){
			flag = fileContentType.equalsIgnoreCase(contentType[u]);
			if(flag) break;
		}
		return flag;
	}
	
	private String rename(String fileName){
		int pos = fileName.lastIndexOf(Constant.DOT);  
	    String ext = fileName.substring(pos);  
	    return RandomUtil.getUniqueKey(false) + ext;  
	}
	
}
