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
public class LongWrapArrConvert implements Convert<Object[],Long[]>{
	
	private static final Logger log = LoggerFactory.getLogger(LongWrapArrConvert.class);

	@Override
	public Long[] convert(Object[] source) {
		if(source == null) return null;  
		Long[] res = new Long[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Long.parseLong(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("LongWrapArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue; 
            }  
        }  
        return res;  
	}

}
