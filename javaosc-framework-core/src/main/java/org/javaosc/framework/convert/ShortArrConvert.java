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
public class ShortArrConvert implements Convert<Object[],Short[]>{
	
	private static final Logger log = LoggerFactory.getLogger(ShortArrConvert.class);

	@Override
	public Short[] convert(Object[] source) {
		if(source == null) return null;  
		Short[] res = new Short[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Short.parseShort(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("WrapShortArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue;
            }  
        }  
        return res;  
	}

}
