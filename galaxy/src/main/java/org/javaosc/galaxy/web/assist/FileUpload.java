package org.javaosc.galaxy.web.assist;

import java.io.File;
import java.io.IOException;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.FileConfig;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.javaosc.galaxy.util.RandomUtil;
import org.javaosc.galaxy.web.multipart.FilePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class FileUpload {
	
	private static final Logger log = LoggerFactory.getLogger(FileUpload.class);
	
	private FilePart filePart;
	
	private String fileDir;
	
	private String fileName;

	public void setLimit(String fileDir,String fileName) {
		this.fileDir = fileDir;
		this.fileName = fileName;
	}
	
	public FileConfig upload() {
		
		if(filePart==null){
			log.error(Constant.GALAXY_EXCEPTION, "upload file part(filePart) must not be null!");
			return null;
		}
		
		if(GalaxyUtil.isEmpty(this.getFileDir())){
			log.error(Constant.GALAXY_EXCEPTION, "upload file path(fileDir) must not be null!");
			return null;
		}
		
		FileConfig fileConfig = new FileConfig();
		
		fileConfig.setOriginalName(filePart.getName());
		fileConfig.setContentType(filePart.getContentType());
		
		String fileName = filePart.getFileName();
	    String realFileName  = getFinalName(fileName);
		fileConfig.setFinalName(realFileName);
		
		fileDir = fileDir.replaceAll(Constant.URL_LINE, Constant.LINE);
		String filePath = fileDir + Constant.LINE + realFileName;
		try {
			File saveFile = new File(filePath);
			if(!saveFile.exists()){
				saveFile.mkdirs();
			}
			long fileSize = filePart.writeTo(saveFile);
			fileConfig.setFileSize(fileSize);
			fileConfig.setFilePath(filePath);
			fileConfig.setCreateTime(System.currentTimeMillis());
			return fileConfig;
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
			return fileConfig;
		}	
	}
	
	public boolean checkSize(int maxSize) {
		if(filePart==null){
			log.error(Constant.GALAXY_EXCEPTION, "upload file part(filePart) must not be null!");
			return false;
		}
		int fileSize = 0;
		try {
			fileSize = filePart.getInputStream().available();
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
			return false;
		}
		return fileSize <= maxSize * 1024 ? true : false;
	}
	
	public boolean checkType(String... contentType) {
		if(filePart==null){
			log.error(Constant.GALAXY_EXCEPTION, "upload file part(filePart) must not be null!");
			return false;
		}
		
		boolean flag = false;
		if(contentType==null || contentType.length==0){
			return true;
		}
		String fileContentType = filePart.getContentType();
		for (int u = 0; u < contentType.length; u++) {
			flag = fileContentType.equalsIgnoreCase(contentType[u]);
			if (flag) break;
		}
		if (!flag) {
			log.error("file format is not supported:" + fileContentType);
		}
		return flag;
	}
	
	private String getFinalName(String fileName){
		String realFileName = null;
		int pos = fileName.lastIndexOf(Constant.DOT);  
	    String ext = fileName.substring(pos);
		if(getFileName()!=null){
			if(GalaxyUtil.isEmpty(getFileName())){
				realFileName = RandomUtil.getUniqueKey(true) + ext;
			}else{
				realFileName = getFileName() + ext;
			}
		}else{
			realFileName = RandomUtil.getUniqueKey(true) + ext;
		}
		return realFileName;
	}
	
	public FilePart getFilePart() {
		return filePart;
	}

	public void setFilePart(FilePart filePart) {
		this.filePart = filePart;
	}

	public String getFileDir() {
		return fileDir;
	}

	public String getFileName() {
		return fileName;
	}

}
