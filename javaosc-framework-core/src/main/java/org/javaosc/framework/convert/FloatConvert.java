package org.javaosc.framework.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class FloatConvert implements Convert<Object, Float> {

	private static final Logger log = LoggerFactory.getLogger(FloatConvert.class);

	@Override
	public Float convert(Object source) {
		if (source == null)
			return null;
		try {
			return Float.parseFloat(String.valueOf(source));
		} catch (NumberFormatException e) {
			log.warn("FloatConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
