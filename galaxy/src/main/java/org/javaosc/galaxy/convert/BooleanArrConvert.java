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
public class BooleanArrConvert implements Convert<Object[],boolean[]>{
	
	private static final Logger log = LoggerFactory.getLogger(BooleanArrConvert.class);

	@Override
	public boolean[] convert(Object[] source) {
		if(source == null) return null;  
		boolean[] res = new boolean[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Boolean.parseBoolean(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("BooleanArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
