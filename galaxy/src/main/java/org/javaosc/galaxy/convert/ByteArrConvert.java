package org.javaosc.galaxy.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ByteArrConvert implements Convert<Object[],byte[]>{
	
	private static final Logger log = LoggerFactory.getLogger(ByteArrConvert.class);

	@Override
	public byte[] convert(Object[] source) {
		if(source == null) return null;  
		byte[] res = new byte[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Byte.parseByte(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("ByteArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
