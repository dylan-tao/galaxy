package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringArrConvert implements Convert<Object[],String[]>{
	
	private static final Logger log = LoggerFactory.getLogger(StringArrConvert.class);

	@Override
	public String[] convert(Object[] source) {
		if(source == null) return null;  
        String[] res = new String[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = String.valueOf(source[i]);  
            } catch (NumberFormatException e) {  
            	log.info("StringArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
