package org.javaosc.galaxy.util;

import java.util.Random;

import org.javaosc.galaxy.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description 随机帮助类
 * @author Dylan Tao
 * @date 2014-10-26
 * Copyright 2014 javaosc.com Team. All Rights Reserved.
 */

public class RandomUtil {

	private static final Logger log = LoggerFactory.getLogger(RandomUtil.class);

	private static long workerId; //机器id
	private static long datacenterId; //工作组id
	private static long sequence = 0L;

	private static long twepoch = 1288834974657L;

	private static long workerIdBits = 5L;
	private static long datacenterIdBits = 5L;
	private static long maxWorkerId = -1L ^ (-1L << workerIdBits); //min-max = 0~31 = 32
	private static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); //min-max = 0~31 = 32
	private static long sequenceBits = 12L;

	private static long workerIdShift = sequenceBits;
	private static long datacenterIdShift = sequenceBits + workerIdBits;
	private static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private static long sequenceMask = -1L ^ (-1L << sequenceBits);

	private static long lastTimestamp = -1L;
	
	
	private static long lastTime = System.currentTimeMillis();
	private static short lastCount = -32768;
	private static Object mutex = new Object();
	
	
	static{
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
		}
	}

	public static synchronized long getId() {
		
		long timestamp = timeGen();

		if (timestamp < lastTimestamp) {
			log.error(String.format("clock is moving backwards.  Rejecting requests until %d.",lastTimestamp));
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	private static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private static long timeGen() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 22位uuid(数字字母组合)主键且有序
	 * @param hr 是否包含横杠
	 * @return String 22位uuid
	 */
	public static String getUniqueKey(boolean hr) {
		long l = 0L;
		short word0 = 0;
		int i = 0;
		synchronized (mutex) {
			if (lastCount == 32767) {
				for (boolean flag = false; !flag;) {
					l = System.currentTimeMillis();
					if (l < lastTime + 1000L) {
//						try {
							Thread.currentThread();
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							log.error(Constant.GALAXY_EXCEPTION, e);
//						}
					} else {
						lastTime = l;
						lastCount = -32768;
						flag = true;
					}
				}
			} else {
				l = lastTime;
			}
			word0 = lastCount++;
			i = getHostUniqueNum();
		}
		String s = Integer.toString(i, 16) + Long.toString(l, 16)
				+ Integer.toString(word0, 16);
		if (!hr) {
			s = s.replace(Constant.HR, Constant.EMPTY);
		}
		if (s.length() > 22) {
			s = s.substring(s.length() - 22);
		}
		log.debug("====== Unique Key: {}", s);
		return s;
	}

	public static String randomNum(int randomLength) {
		char[] randoms = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(randoms[random.nextInt(randoms.length)]);
		}
		random = null;
		return ret.toString();
	}
	
	private static int getHostUniqueNum() {
		return (new Object()).hashCode();
	}
	
}
