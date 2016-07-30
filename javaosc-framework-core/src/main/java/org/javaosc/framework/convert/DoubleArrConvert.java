package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleArrConvert implements Convert<Object[],double[]>{
	
	private static final Logger log = LoggerFactory.getLogger(DoubleArrConvert.class);

	@Override
	public double[] convert(Object[] source) {
		if(source == null) return null;  
        double[] res = new double[source.length];  
        for(int i=0;i<source.length;i++){  
            try {  
                res[i] = Double.parseDouble(String.valueOf(source[i]));  
            } catch (NumberFormatException e) {  
            	log.info("DoubleArrConvert failed, value: {} exception: {}", String.valueOf(source[i]), e);  
                return null;  
            }  
        }  
        return res;  
	}

}
