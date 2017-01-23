package org.javaosc.galaxy.constant;


/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Constant {
	
	String BR = System.getProperty("line.separator");
	
	String GALAXY_EXCEPTION = " === Please see the following detailed error info: " + BR;
	
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
	String JSON_PARAM = "_JSON_PARAM_";
	String CONTENT_TYPE = "_CONTENT_PARAM_";
	
	
	// http协议
	public enum HttpType {
		GET, POST, PUT, DELETE;
	}
	
	public enum ReqContentType {
		
		WWW_FORM_URLENCODED("application/x-www-form-urlencoded"), MULTIPART_FORM_DATA("multipart/form-data"),APPLICATION_JSON("application/json"),TEXT_XML("text/xml");

		private final String value;

		ReqContentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	// 响应类型
	public enum ResContentType {
		
		TEXT("text/plain"), JSON("application/json"), XML("text/xml"), HTML("text/html"), JAVASCRIPT("text/javascript");

		private final String value;

		ResContentType(String value) {
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
		
		 public static CodeType getCodeType(String value) {
			 for (CodeType codeType : CodeType.values()) {
		         if (codeType.getValue().equals(value)) {
		            return codeType;
		         }    
			 }
			 return null;   
		 }
		
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
