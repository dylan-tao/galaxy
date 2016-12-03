package org.javaosc.galaxy.constant;


/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Constant {
	
	String GALAXY_EXCEPTION = " === galaxy execution exception, please see the following Caused by: " + System.getProperty("line.separator");
	
	String SUFFIX_CLASS = ".class";
	String DOT = ".";
	String URL_LINE = "/";
	String COLON = ":";
	String COLON_EXTEND = "：";
	String COMMA = ",";
	String DOUBLE_LINE = "//";
	String EMPTY = "";
	String SPACE = " ";
	String HR = "-";
	String UNDER_LINE = "_";
	String QM = "?";
	String EM = "=";
	String AM = "&";
	String JZ = "#";
	String LINE = System.getProperty("file.separator");
	
	// http协议
	public enum HttpType {
		GET, POST, PUT, DELETE;
	}
	
	// 响应类型
	public enum ContentType {
		
		TEXT("text/plain"), JSON("application/json"), XML("text/xml"), HTML("text/html"), JAVASCRIPT("text/javascript");

		private final String value;

		ContentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	// 编码
	public enum CodeType {

		UTF8("UTF-8"),GBK("GBK"),GB2312("GB2312"),ISO88591("ISO-8859-1"),GB18030("GB18030"), UTF16("UTF16");

		private final String value;

		CodeType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
