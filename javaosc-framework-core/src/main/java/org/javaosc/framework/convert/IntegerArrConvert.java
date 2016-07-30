package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerArrConvert implements Convert<Object[],int[]>{
	
	private static final Logger log = LoggerFactory.getLogger(IntegerArrConvert.class);

	@Override
	public int[] convert(Object[] source) {
		if(source == null) return null;  
        int[] res = new int[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Integer.parseInt(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("IntegerArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
