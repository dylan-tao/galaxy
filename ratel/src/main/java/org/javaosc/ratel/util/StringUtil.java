package org.javaosc.ratel.util;

import org.javaosc.ratel.constant.Constant;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class StringUtil {

	/**
	 * 非空判断
	 * @param str 要判断的字符串
	 * @return boolean true/false
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 空判断
	 * @param str 要判断的字符串
	 * @return boolean true/false
	 */
	public static boolean isBlank(String str) {
		if(str == null || str.length() <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 清除空格
	 * @param str 要处理的字符串
	 * @param index ALL所有/LANDR左右/LEFT左边/RIGHT右边
	 * @return String 处理后的字符串
	 */
	public static String clearSpace(String str, PatternValue index) {
		return str.replaceAll(index.getValue(), Constant.EMPTY);
	}
	
	public static String join(String[] stringArray) {
		StringBuilder sb = new StringBuilder();
		for (String s : stringArray){
			sb.append(s);
		}
		return sb.toString();
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
		 if(StringUtil.isNotBlank(value)){
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
	       if (StringUtil.isBlank(param)){  
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
		   if (StringUtil.isBlank(param)){  
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
	
	//位置枚举
	public enum PatternValue {

		LEFT {
			public String getValue() {
				return "^\\s*";
			}
		},
		RIGHT {
			public String getValue() {
				return "\\s*$";
			}
		},
		LANDR {
			public String getValue() {
				return "(^\\s*)|(\\s*$)";
			}
		},
		ALL {
			public String getValue() {
				return "\\s+";
			}
		};

		public abstract String getValue();
	}
	
}
