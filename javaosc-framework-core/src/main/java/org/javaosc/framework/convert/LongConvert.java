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
public class LongConvert implements Convert<Object, Long> {

	private static final Logger log = LoggerFactory.getLogger(LongConvert.class);

	@Override
	public Long convert(Object source) {
		if (source == null)
			return null;
		try {
			return Long.parseLong(String.valueOf(source));
		} catch (NumberFormatException e) {
			log.info("LongConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
