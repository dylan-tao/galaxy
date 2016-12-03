package org.javaosc.galaxy.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.javaosc.galaxy.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description 
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 javaosc Team. All Rights Reserved.
 */
public class HeaderHexUtil {
	
	private static final Logger log = LoggerFactory.getLogger(HeaderHexUtil.class);
	
	public static boolean check(InputStream is, String[] headerCode){
		boolean flag = false;
		if(headerCode!=null){
			String fileHeaderCode = HeaderHexUtil.getHeaderCode(is);
			for(int i=0;i<headerCode.length;i++){
				if(fileHeaderCode.equals(headerCode[i])){
					flag = true;
					break;
				}
			}
		}else{
			flag = true;
		}
		return flag;
	}
	
	protected static String bytesToHexString(byte[] fileByte) {
		StringBuilder stringBuilder = new StringBuilder();
		if (fileByte == null || fileByte.length <= 0) {
			return null;
		}
		for (int i = 0; i < fileByte.length; i++) {
			int v = fileByte[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	protected static String getHeaderCode(InputStream is) {
		try {
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			return bytesToHexString(b);
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return null;
	}

	protected static String getHeaderCode(String filePath) {
		try {
			FileInputStream is = new FileInputStream(filePath);
			return getHeaderCode(is);
		} catch (FileNotFoundException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return null;
	}
}
