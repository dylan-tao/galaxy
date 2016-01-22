package org.javaosc.framework.web.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class PutPrmAssist {

	private HttpServletRequest request;

	public PutPrmAssist(HttpServletRequest request) {
		this.request = request;
	}
	
	public HashMap<String, Object> getData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		FileItemFactory factory = new DiskFileItemFactory();
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	    Iterator<FileItem> itr = items.iterator();
	    while (itr.hasNext()) {
	       FileItem item = (FileItem) itr.next();
	       if (item.isFormField()) {
	    	   map.put(item.getFieldName(), item.getString());
	       }
	    }
		return map;	
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
