package org.javaosc.framework.assist;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.CodeType;
import org.javaosc.framework.constant.Constant.HttpType;
import org.javaosc.framework.context.ConfigurationHandler;
import org.javaosc.framework.web.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParamHandler {
	
	private static final Logger log = LoggerFactory.getLogger(RequestParamHandler.class);

	public static HashMap<String, String[]> getFormatData(HttpServletRequest request,HttpServletResponse response) {

		HashMap<String, String[]> dataMap = new HashMap<String, String[]>();

		Enumeration<?> e = request.getParameterNames();
		String requestType = request.getMethod();

		if (ConfigurationHandler.getRequestEncode() && (HttpType.GET.toString().equalsIgnoreCase(requestType) || HttpType.DELETE.toString().equalsIgnoreCase(requestType))) { // get\delete
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String[] values = request.getParameterValues(key);
				if (values == null) {
					dataMap.put(key, null);
				} else {
					String[] newValues = new String[values.length];
					for (String newValue : newValues) {
						newValue = encodingParam(newValue);
					}
					dataMap.put(key, newValues);
				}
			}
		} else { // post\put\get not open encode support
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String[] values = request.getParameterValues(key);
				if (values == null) {
					dataMap.put(key, null);
				} else {
					dataMap.put(key, values);
				}
			}
		}

		return dataMap;
	}
	
	public static void put(String key, String value){
		if(ConfigurationHandler.getRequestEncode()){
			value = encodingParam(value);
		}
		ActionContext.getContext().put(key, value);
	}

	private static String encodingParam(String value) {
		try {
			byte[] b = value.getBytes(CodeType.ISO88591.getValue());
			value = new String(b, ConfigurationHandler.getContextEncode());
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}
		return value;
	}
}
