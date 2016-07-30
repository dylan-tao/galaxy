package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteArrConvert implements Convert<Object[],byte[]>{
	
	private static final Logger log = LoggerFactory.getLogger(ByteArrConvert.class);

	@Override
	public byte[] convert(Object[] source) {
		if(source == null) return null;  
		byte[] res = new byte[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Byte.parseByte(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("ByteArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
