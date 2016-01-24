package org.javaosc.framework.constant;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Constant {
	
	String JAVAOSC_EXCEPTION = "javaosc execution exception ";

	String SET_ENCODING_KEY = "encoding";
	String SET_ENFORC_ENCODING_KEY = "forceEncoding";
	String SET_HTTPGET_ENCODING_KEY = "httpGetEncoding";
	String SET_STATIC_CACHE_KEY = "staticCache";

	String SUFFIX_CLASS = ".class";
	String DOT = ".";
	String LINE = "/";
	String COLON = ":";
	String COLON_EXTEND = "：";
	String COMMA = ",";
	String DOUBLE_LINE = "//";
	String EMPTY = "";
	String SPACE = " ";
	String HR = "-";
	String QM = "?";
	String EM = "=";
	String AM = "&";

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
	public enum CodeConstant {

		UTF8("UTF-8"),GBK("GBK"),GB2312("GB2312"),ISO88591("ISO-8859-1"),GB18030("GB18030"), UTF16("UTF16");

		private final String value;

		CodeConstant(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}
	
	// 存储方式
	public enum StorageRule {

		YEAR {
			public int getValue() {
				return 0;
			}
		},
		MONTH {
			public int getValue() {
				return 1;
			}
		},
		DAY {
			public int getValue() {
				return 2;
			}
		},
		HOUR {
			public int getValue() {
				return 3;
			}
		};

		public abstract int getValue();
	}

}
