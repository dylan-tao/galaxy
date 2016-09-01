package com.javaosc.user.action;

import org.javaosc.framework.annotation.Autowired;
import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.web.assist.ContextHandler;

import com.javaosc.user.service.UserService;

@Mapping("/user")
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Mapping("/{userId}")
	public void getUserList(User user){
		user = userService.getUserList(user);
		ContextHandler.putJson(user);
	}

}
