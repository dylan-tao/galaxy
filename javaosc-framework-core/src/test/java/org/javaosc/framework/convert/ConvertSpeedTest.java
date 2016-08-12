package org.javaosc.framework.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.javaosc.framework.assist.PropertyConvert;
import org.javaosc.framework.util.JsonUtil;

public class ConvertSpeedTest {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "帝州");
		map.put("hobby", new String[]{"1","2"});
		map.put("ha", new String[]{"11111","222222"});
		map.put("gaga", new String[]{"33333","44444"});
		map.put("ga", new String[]{"3333333333","44444444444"});
		map.put("age", 12);
		map.put("ages", 1444);
		
		for(int p=0;p<10;p++){
			
			
			/* cglib */
			long totalTime2 = 0;
			User u2 = null;
			for(int i=0;i<10000;i++){
				long time = System.currentTimeMillis();
				u2 = CglibConvert.convertMapToEntity(map, User.class);
				long charTime = System.currentTimeMillis() - time;
				totalTime2 = totalTime2 + charTime;
			}
//			System.out.println(JsonUtil.toJson(u2));
			System.out.println("cglib convert: " + totalTime2);
			
			/* javaosc */
			long totalTime = 0;
			User u3 = null;
			for(int i=0;i<10000;i++){
				long time = System.currentTimeMillis();
				u3 = PropertyConvert.convertMapToEntity(map, User.class);
				long charTime = System.currentTimeMillis() - time;
				totalTime = totalTime + charTime;
			}
//			System.out.println(JsonUtil.toJson(u3));
			System.out.println("javaosc convert: " + totalTime);
			
			/* beanUtil */
			long totalTime3 = 0;
			User u = new User();
			for(int i=0;i<10000;i++){
				long time = System.currentTimeMillis();
				BeanUtils.populate(u, map);
				long charTime = System.currentTimeMillis() - time;
				totalTime3 = totalTime3 + charTime;
			}
//			System.out.println(JsonUtil.toJson(u));
			System.out.println("beanUtil convert: " + totalTime3);
			
			System.out.println("-----------------------------------");
			
		}
		
		
//		System.out.println(JsonUtil.toJson(user));
		
		
	
		
	
		
	}

}
