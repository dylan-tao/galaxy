package org.javaosc.framework.convert;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ConvertFactory {
	
	private static final Logger log = LoggerFactory.getLogger(ConvertFactory.class);
	
	static final String PREFIX = "_$PARAM_TYPE_";
	
	public static Map<String,Convert<?,?>> typeConvert = new HashMap<String, Convert<?,?>>();
	
	static{
		
		typeConvert.put(PREFIX + Integer.class.getName(), new IntegerConvert());
		typeConvert.put(PREFIX + String.class.getName(), new StringConvert());
		typeConvert.put(PREFIX + Long.class.getName(), new LongConvert());
		typeConvert.put(PREFIX + Byte.class.getName(), new ByteConvert());
		typeConvert.put(PREFIX + Double.class.getName(), new DoubleConvert());
		typeConvert.put(PREFIX + Short.class.getName(), new ShortConvert());
		typeConvert.put(PREFIX + Float.class.getName(), new FloatConvert());
		
		typeConvert.put(PREFIX + Integer[].class.getName(), new IntegerArrConvert());
		typeConvert.put(PREFIX + String[].class.getName(), new StringArrConvert());
		typeConvert.put(PREFIX + Long[].class.getName(), new LongArrConvert());
		typeConvert.put(PREFIX + Byte[].class.getName(), new ByteArrConvert());
		typeConvert.put(PREFIX + Double[].class.getName(), new DoubleArrConvert());
		typeConvert.put(PREFIX + Short[].class.getName(), new ShortArrConvert());
		typeConvert.put(PREFIX + Float[].class.getName(), new FloatArrConvert());
		
		typeConvert.put(PREFIX + int.class.getName(), new IntegerConvert());
		typeConvert.put(PREFIX + long.class.getName(), new LongConvert());
		typeConvert.put(PREFIX + byte.class.getName(), new ByteConvert());
		typeConvert.put(PREFIX + double.class.getName(), new DoubleConvert());
		typeConvert.put(PREFIX + short.class.getName(), new ShortConvert());
		typeConvert.put(PREFIX + float.class.getName(), new FloatConvert());
		
		typeConvert.put(PREFIX + int[].class.getName(), new IntegerArrConvert());
		typeConvert.put(PREFIX + long[].class.getName(), new LongArrConvert());
		typeConvert.put(PREFIX + byte[].class.getName(), new ByteArrConvert());
		typeConvert.put(PREFIX + double[].class.getName(), new DoubleArrConvert());
		typeConvert.put(PREFIX + short[].class.getName(), new ShortArrConvert());
		typeConvert.put(PREFIX + float[].class.getName(), new FloatArrConvert());
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convert(Class<T> cls,Object val){  
		 Convert cv = typeConvert.get(PREFIX + cls.getName());  
	     if(cv == null){  
	        log.info("{} convert failed: unsupport type, value: {}", cls.getName(), val);  
	        return null;  
	     }  
	     return (T)cv.convert(val);
	}  
	
}
