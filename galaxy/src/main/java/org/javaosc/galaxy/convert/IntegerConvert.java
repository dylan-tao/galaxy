package org.javaosc.galaxy.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class IntegerConvert implements Convert<Object, Integer> {

	private static final Logger log = LoggerFactory.getLogger(IntegerConvert.class);

	@Override
	public Integer convert(Object source) {
		if (source == null)
			return null;
		try {
			return Integer.parseInt(String.valueOf(source));	
		} catch (NumberFormatException e) {
			log.warn("IntegerConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
