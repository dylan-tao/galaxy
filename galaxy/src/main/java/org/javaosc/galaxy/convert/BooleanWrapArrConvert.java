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
public class BooleanWrapArrConvert implements Convert<Object[],Boolean[]>{
	
	private static final Logger log = LoggerFactory.getLogger(BooleanWrapArrConvert.class);

	@Override
	public Boolean[] convert(Object[] source) {
		if(source == null) return null;  
		Boolean[] res = new Boolean[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Boolean.parseBoolean(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("BooleanWrapArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
