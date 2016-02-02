package org.javaosc.framework.web.util;

import org.javaosc.framework.constant.Constant;

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
