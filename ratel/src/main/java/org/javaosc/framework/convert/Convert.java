package org.javaosc.framework.convert;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface Convert<S,T> {  
	  
    T convert(S source);  
}  
