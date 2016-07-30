package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrapDoubleArrConvert implements Convert<Object[],Double[]>{
	
	private static final Logger log = LoggerFactory.getLogger(WrapDoubleArrConvert.class);

	@Override
	public Double[] convert(Object[] source) {
		if(source == null) return null;  
		Double[] res = new Double[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Double.parseDouble(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("WrapDoubleArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
