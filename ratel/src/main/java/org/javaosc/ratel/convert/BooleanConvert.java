package org.javaosc.ratel.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class BooleanConvert implements Convert<Object, Boolean> {

	private static final Logger log = LoggerFactory.getLogger(BooleanConvert.class);

	@Override
	public Boolean convert(Object source) {
		if(source == null){
			return false;
		}else{
			try {
				return Boolean.parseBoolean(String.valueOf(source));
			} catch (NumberFormatException e) {
				log.warn("BooleanConvert failed, value: {} exception: {}", String.valueOf(source), e);
				return false;
			}
		}
	}

}
