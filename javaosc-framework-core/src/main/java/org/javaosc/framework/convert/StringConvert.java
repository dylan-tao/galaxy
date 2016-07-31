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
public class StringConvert implements Convert<Object[], String> {

	private static final Logger log = LoggerFactory.getLogger(StringConvert.class);

	@Override
	public String convert(Object[] source) {
		if (source == null)
			return null;
		try {
			return String.valueOf(source[0]);
		} catch (NumberFormatException e) {
			log.warn("StringConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
