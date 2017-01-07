package com.javaosc.user.action;

import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.web.assist.ContextHandler;

@Mapping("/user")
public class UserAction {
	
	@Mapping("/hello")
	public String getUserList(Byte flag){
		if(flag==null || flag==0){
			ContextHandler.putAttr("h","world");
			return "index";
		}else{
			ContextHandler.putJson("h","world");
			return null;
		}
		
	}
}
