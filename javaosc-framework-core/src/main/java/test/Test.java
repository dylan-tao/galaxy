package test;

import java.util.HashMap;

import org.javaosc.framework.assist.PropertyConvert;
import org.javaosc.framework.util.JsonUtil;

public class Test {

	public static void main(String[] args) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "帝州");
		map.put("hobby", new String[]{"1","2"});
		map.put("ha", new String[]{"11111","222222"});
		map.put("gaga", new String[]{"33333","44444"});
		map.put("ga", new String[]{"3333333333","44444444444"});
		map.put("age", 12);
		map.put("ages", 1444);
		
		User user = PropertyConvert.convertMapToEntity(map, User.class);
		System.out.println(JsonUtil.toJson(user));
	}

}
