package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            	log.info("LongArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
