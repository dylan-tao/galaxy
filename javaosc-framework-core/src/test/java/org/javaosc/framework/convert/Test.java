package org.javaosc.framework.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.javaosc.framework.assist.PropertyConvert;
import org.javaosc.framework.util.JsonUtil;

public class Test {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "帝州");
		map.put("hobby", new String[]{"1","2"});
		map.put("ha", new String[]{"11111","222222"});
		map.put("gaga", new String[]{"33333","44444"});
		map.put("ga", new String[]{"3333333333","44444444444"});
		map.put("age", 12);
		map.put("ages", 1444);
		
//		long totalTime = 0;
//		for(int i=0;i<10000;i++){
//			long time = System.currentTimeMillis();
//			PropertyConvert.convertMapToEntity(map, User.class);
//			long charTime = System.currentTimeMillis() - time;
//			System.out.println(charTime);
//			totalTime = totalTime + charTime;
//		}
//		System.out.println(totalTime);
		
//		System.out.println(JsonUtil.toJson(user));
		
		
		long totalTime = 0;
		for(int i=0;i<10000;i++){
			long time = System.currentTimeMillis();
			BeanUtils.populate(new User(), map);
			long charTime = System.currentTimeMillis() - time;
			System.out.println(charTime);
			totalTime = totalTime + charTime;
		}
		System.out.println(totalTime);
	}

}
