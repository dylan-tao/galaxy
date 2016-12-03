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
public class DoubleWrapArrConvert implements Convert<Object[],Double[]>{
	
	private static final Logger log = LoggerFactory.getLogger(DoubleWrapArrConvert.class);

	@Override
	public Double[] convert(Object[] source) {
		if(source == null) return null;  
		Double[] res = new Double[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Double.parseDouble(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("DoubleWrapArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
