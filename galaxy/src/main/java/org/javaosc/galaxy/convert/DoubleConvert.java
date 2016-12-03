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
public class DoubleConvert implements Convert<Object, Double> {

	private static final Logger log = LoggerFactory.getLogger(DoubleConvert.class);

	@Override
	public Double convert(Object source) {
		if (source == null)
			return null;
		try {
			return Double.parseDouble(String.valueOf(source));
		} catch (NumberFormatException e) {
			log.warn("DoubleConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
