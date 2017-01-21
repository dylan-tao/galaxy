package com.javaosc.user.action;

import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.web.assist.ContextHandler;

@Mapping("/user")
public class UserAction {
	
	@Mapping("/hello")
	public String getUserList(User user){
		ContextHandler.putJson(user);
		return null;
	}
}
