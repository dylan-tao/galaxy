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
public class FloatArrConvert implements Convert<Object[],float[]>{
	
	private static final Logger log = LoggerFactory.getLogger(FloatArrConvert.class);

	@Override
	public float[] convert(Object[] source) {
		if(source == null) return null;  
		float[] res = new float[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Float.parseFloat(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.warn("FloatArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
            	continue;  
            }  
        }  
        return res;  
	}

}
