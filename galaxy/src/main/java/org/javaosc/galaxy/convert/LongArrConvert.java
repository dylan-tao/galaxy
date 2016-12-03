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
public class LongArrConvert implements Convert<Object[],long[]>{
	
	private static final Logger log = LoggerFactory.getLogger(LongArrConvert.class);

	@Override
	public long[] convert(Object[] source) {
		if(source == null) return null;  
		long[] res = new long[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Long.parseLong(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("LongArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
