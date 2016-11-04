package org.javaosc.ratel.web.assist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.constant.Constant.CodeType;
import org.javaosc.ratel.constant.FileConfig;
import org.javaosc.ratel.constant.UploadConfig;
import org.javaosc.ratel.util.CodeUtil;
import org.javaosc.ratel.util.HeaderHexUtil;
import org.javaosc.ratel.util.StringUtil;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class FileUpload {
	
	private String fileDir;
	
	private String[] fileName;

	private int maxSize;

	private String[] headerCode;
	
	public FileUpload(String fileDir, int maxSize, String... headerCode) {
		this.fileDir = fileDir;
		this.maxSize = maxSize;
		this.headerCode = headerCode;
	}
	
	public FileUpload(String fileDir,String[] fileName, int maxSize, String... headerCode) {
		this.fileDir = fileDir;
		this.fileName = fileName;
		this.maxSize = maxSize;
		this.headerCode = headerCode;
	}
	
	public UploadConfig getInputStream(HttpServletRequest request) {
		
		UploadConfig config = new UploadConfig();
		
		if(StringUtil.isNotBlank(this.getFileDir())){
			try {
				MultipartParser mp = new MultipartParser(request, this.getMaxSize());
				mp.setEncoding(request.getCharacterEncoding());
				Part part;
				int p = 0;
				
				while ((part = mp.readNextPart()) != null) {
					if (part.isFile()) {
						FileConfig fileConfig = new FileConfig();
						fileConfig.setOriginalFilename(part.getName());
						FilePart filePart = (FilePart)part;
						InputStream is = filePart.getInputStream();
						fileConfig.setContentType(filePart.getContentType());
						fileConfig.setStream(is);
						
						String fileName = filePart.getFileName();
						if(HeaderHexUtil.check(is, headerCode)){
							String realFileName = null;
							int pos = fileName.lastIndexOf(Constant.DOT);  
						    String ext = fileName.substring(pos);
						    
							if(this.getFileName()!=null && this.getFileName().length>0){
								realFileName = this.getFileName()[p] + ext;
								p++;
							}else{
								realFileName = CodeUtil.encodeMD5(fileName, false, CodeType.UTF8) + ext;
							}
							fileConfig.setFileName(realFileName);
							fileConfig.setCode(0);
							fileConfig.setCreateTime(System.currentTimeMillis());
						}else{	//不符合标准的文件
							fileConfig.setCode(-1);
						}
						config.putFile(fileConfig);
					}else if(part.isParam()){
						ParamPart paramPart = (ParamPart) part;
				        config.putParam(paramPart.getName(), paramPart.getStringValue());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//存储路径不能为空
		}
		return config;
	}
	
	public UploadConfig upload(HttpServletRequest request) {
		
		UploadConfig config = new UploadConfig();
		
		if(StringUtil.isNotBlank(this.getFileDir())){
			try {
				MultipartParser mp = new MultipartParser(request, this.getMaxSize());
				mp.setEncoding(request.getCharacterEncoding());
				Part part;
				int p = 0;
				
				while ((part = mp.readNextPart()) != null) {
					if (part.isFile()) {
						FileConfig fileConfig = new FileConfig();
						fileConfig.setOriginalFilename(part.getName());
						FilePart filePart = (FilePart)part;
						InputStream is = filePart.getInputStream();
						fileConfig.setContentType(filePart.getContentType());
						
						String fileName = filePart.getFileName();
						if(HeaderHexUtil.check(is, headerCode)){
							String realFileName = null;
							int pos = fileName.lastIndexOf(Constant.DOT);  
						    String ext = fileName.substring(pos);
						    
							if(this.getFileName()!=null && this.getFileName().length>0){
								realFileName = this.getFileName()[p] + ext;
								p++;
							}else{
								realFileName = CodeUtil.encodeMD5(fileName, false, CodeType.UTF8) + ext;
							}
							fileConfig.setFileName(realFileName);
						
							File saveFile = new File(fileDir + Constant.LINE + realFileName);
							if(!saveFile.exists()){
								saveFile.mkdirs();
							}
							long fileSize = filePart.writeTo(saveFile);
							fileConfig.setFileSize(fileSize);
							fileConfig.setCode(0);
							fileConfig.setCreateTime(System.currentTimeMillis());
						}else{	//不符合标准的文件
							fileConfig.setCode(-1);
						}
						config.putFile(fileConfig);
					}else if(part.isParam()){
						ParamPart paramPart = (ParamPart) part;
				        config.putParam(paramPart.getName(), paramPart.getStringValue());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//存储路径不能为空
		}
		return config;
	}
	
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public void setHeaderCode(String[] headerCode) {
		this.headerCode = headerCode;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}

	private String[] getFileName() {
		return fileName;
	}

	private int getMaxSize() {
		return maxSize;
	}

	private String getFileDir() {
		return fileDir;
	}

	public String[] getHeaderCode() {
		return headerCode;
	}
	
}
