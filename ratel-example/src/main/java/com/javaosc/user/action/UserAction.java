package com.javaosc.user.action;

import org.javaosc.ratel.annotation.Autowired;
import org.javaosc.ratel.annotation.Mapping;
import org.javaosc.ratel.web.assist.ContextHandler;

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
