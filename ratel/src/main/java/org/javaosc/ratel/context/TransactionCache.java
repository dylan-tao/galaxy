package org.javaosc.ratel.context;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TransactionCache {
	
	private static HashMap<String, Boolean> cache;
	
	static{
		cache = new HashMap<String, Boolean>();
	}

	public static boolean get(Method method) {
		Boolean flag = cache.get(method.toString());
		if(flag!=null){
			return flag;
		}else{
			flag = true;
			if(ConfigHandler.getMethodKeyword()!=null){
				for(String keyword:ConfigHandler.getMethodKeyword()){
					if(method.getName().startsWith(keyword)){
						flag = false;
						break;
					}
				}
			}
			cache.put(method.toString(), flag);
			return flag;
		}
	}

}
