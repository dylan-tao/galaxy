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
public class ShortConvert implements Convert<Object, Short> {

	private static final Logger log = LoggerFactory.getLogger(ShortConvert.class);

	@Override
	public Short convert(Object source) {
		if (source == null)
			return null;
		try {
			return Short.parseShort(String.valueOf(source));
		} catch (NumberFormatException e) {
			log.warn("ShortConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
