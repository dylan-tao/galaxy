package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ByteWrapArrConvert implements Convert<Object[],Byte[]>{
	
	private static final Logger log = LoggerFactory.getLogger(ByteWrapArrConvert.class);

	@Override
	public Byte[] convert(Object[] source) {
		if(source == null) return null;  
		Byte[] res = new Byte[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Byte.parseByte(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("ByteWrapArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
