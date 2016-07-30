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
public class WrapIntegerArrConvert implements Convert<Object[],Integer[]>{
	
	private static final Logger log = LoggerFactory.getLogger(WrapIntegerArrConvert.class);

	@Override
	public Integer[] convert(Object[] source) {
		if(source == null) return null;  
		Integer[] res = new Integer[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Integer.parseInt(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("WrapIntegerArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
