package com.javaosc.user.action;

import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Ref;
import org.javaosc.framework.util.JsonUtil;

import com.javaosc.user.service.UserService;

@Mapping("/user")
public class UserAction {
	
	@Ref
	private UserService userService;
	
	@Mapping("/{userId}")
	public void getUserList(User user){
		user = userService.getUserList(user);
		System.out.println(JsonUtil.toJson(user));
	}

}
