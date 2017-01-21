package org.javaosc.galaxy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.PatternValue;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class GalaxyUtil {
	
	 public static boolean isEmpty(Map<?, ?> map) {
		 if (map == null || map.size() == 0) {
	         return true;
	     }
	     return false;  
	 }
	 
	 public static boolean isEmpty(List<?> list) {
	     if (list == null || list.size() == 0) {
	         return true;
	     }
	     return false;
	 }
	 
	 public static boolean isEmpty(String s) {
		 if (s == null || s.length() == 0) {
	          return true;
	      }
	     return false;   
	 }
	
	public static String clearSpace(String str, PatternValue index) {
		return str.replaceAll(index.getValue(), Constant.EMPTY);
	}
	
	public static String join(String[] stringArray, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<stringArray.length; i++) {
			if(i>0){
				sb.append(separator);
			}
			sb.append(stringArray[i]);
		}
		return sb.toString();
	}
	
	/**
	 * first char format
	 * @param value
	 * @param flag true:lowerCase,false:upperCase
	 * @return
	 */
	 public static String formatFirstChar(String value, boolean flag){
		 if(!GalaxyUtil.isEmpty(value)){
			 char[] cs = value.toCharArray();
			 if(!flag && Character.isLowerCase(cs[0])){
				 cs[0]-=32;
			 }else if(flag && Character.isUpperCase(cs[0])){
				 cs[0]+=32;
			 }
			 value = String.valueOf(cs);
		 }
		 return value;
	 }
	 
	 public static String camelToUnderline(String param){  
	       if (GalaxyUtil.isEmpty(param)){  
	           return Constant.EMPTY;  
	       }  
	       int len=param.length();  
	       StringBuilder sb=new StringBuilder(len);  
	       for (int i = 0; i < len; i++) {  
	           char c=param.charAt(i);  
	           if (Character.isUpperCase(c)){  
	               sb.append('_');  
	               sb.append(Character.toLowerCase(c));  
	           }else{  
	               sb.append(c);  
	           }  
	       }  
	       return sb.toString();  
	 }  
	 
	 public static String underlineToCamel(String param){  
		   if (GalaxyUtil.isEmpty(param)){  
	           return Constant.EMPTY;  
	       }  
	       int len=param.length();  
	       StringBuilder sb=new StringBuilder(len);  
	       for (int i = 0; i < len; i++) {  
	           char c=param.charAt(i);  
	           if (c=='_'){  
	              if (++i<len){  
	                  sb.append(Character.toUpperCase(param.charAt(i)));  
	              }  
	           }else{  
	               sb.append(c);  
	           }  
	       }  
	       return sb.toString();  
	}  
	 
	public static HashMap<String, String> getQueryArray(String queryString){
		HashMap<String, String> queryMap = new HashMap<String, String>();
		String[] params = null;
		if(queryString.indexOf(Constant.AM)>0){
			params = queryString.split(Constant.AM);
		}else{
			params = new String[]{queryString};
		}
		if(params!=null){
			for(String param:params){
				String[] paramArray = param.split(Constant.EM);
				queryMap.put(paramArray[0], paramArray[1]);  
			}
			
		}
		return queryMap;
	}
	
}
