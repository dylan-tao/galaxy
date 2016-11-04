package org.javaosc.ratel.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class UploadConfig {
	
	private HashMap<String, Object> params;
	
	private List<FileConfig> files;

	public void putFile(FileConfig file) {
		if(files==null){
			files = new ArrayList<FileConfig>();
		}
		files.add(file);
	}
	
	public void putParam(String key,String value){
		if(params==null){
			params = new HashMap<String, Object>();
		}
		params.put(key, value);
	}
	
}
