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
public class ByteConvert implements Convert<Object, Byte> {

	private static final Logger log = LoggerFactory.getLogger(ByteConvert.class);

	@Override
	public Byte convert(Object source) {
		if (source == null)
			return null;
		try {
			return Byte.parseByte(String.valueOf(source));
		} catch (NumberFormatException e) {
			log.info("ByteConvert failed, value: {} exception: {}", String.valueOf(source), e);
			return null;
		}
	}

}
