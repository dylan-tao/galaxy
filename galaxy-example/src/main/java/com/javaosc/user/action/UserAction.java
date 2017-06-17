package com.javaosc.user.action;

import org.javaosc.galaxy.annotation.Autowired;
import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.web.assist.Context;

import com.javaosc.user.service.UserService;

@Mapping("/user")
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Mapping("/hello")
	public String getUserList(User user){
		Context.putJson(user);
		return null;
	}
	
	@Mapping("/{userId}")
	public String getUserById(User user){
		Context.putJson(userService.getUserList(user));
		return null;
	}
}
