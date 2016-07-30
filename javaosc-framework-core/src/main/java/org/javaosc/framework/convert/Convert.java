package org.javaosc.framework.convert;

public interface Convert<S,T> {  
	  
    T convert(S source);  
}  
