package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrapFloatArrConvert implements Convert<Object[],Float[]>{
	
	private static final Logger log = LoggerFactory.getLogger(WrapFloatArrConvert.class);

	@Override
	public Float[] convert(Object[] source) {
		if(source == null) return null;  
		Float[] res = new Float[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Float.parseFloat(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("WrapFloatArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
