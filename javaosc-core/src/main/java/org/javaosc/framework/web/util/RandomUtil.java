package org.javaosc.framework.web.util;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.javaosc.framework.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class RandomUtil {

	private static final Logger log = LoggerFactory.getLogger(RandomUtil.class);

	private static long lastTime = System.currentTimeMillis();
	private static short lastCount = -32768;
	private static Object mutex = new Object();

	private static final Lock LOCK = new ReentrantLock();
	private static short lastNum = 0;
	private static int count = 0;

	/**
	 * 获取唯一且有序的16位的数字(集群需要组合机器码/ip)
	 * @return long 16位
	 */
	@SuppressWarnings("finally")
	public static long getUniqueNum() {
		LOCK.lock();
		try {
			if (lastNum == 10) {
				boolean done = false;
				while (!done) {
					long now = System.currentTimeMillis();
					if (now == lastTime) {
						try {
							Thread.currentThread();
							Thread.sleep(1);
						} catch (java.lang.InterruptedException e) {
						}
						continue;
					} else {
						lastTime = now;
						lastNum = 0;
						done = true;
					}
				}
			}
			count = lastNum++;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally {
			LOCK.unlock();
            String lastReturnNum = lastTime + Constant.EMPTY + String.format("%03d",count);
            log.debug("====== Unique Number: " + lastReturnNum);
            return Long.parseLong(lastReturnNum); 
		}
	}

	/**
	 * 自制22位uuid(数字字母组合)主键且有序
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
						try {
							Thread.currentThread();
							Thread.sleep(1000);
						} catch (InterruptedException interruptedexception) {
							log.error(interruptedexception);
						}
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
		log.debug("====== Unique Key: " + s);
		return s;
	}
	
	/**
	 * 生成uuid
	 * @return String 返回uuid字符串
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
	    return uuid.toString().toUpperCase().replaceAll("-", "");
	}

	private static int getHostUniqueNum() {
		return (new Object()).hashCode();
	}
	
}
