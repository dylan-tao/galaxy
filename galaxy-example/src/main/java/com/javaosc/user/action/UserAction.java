package com.javaosc.user.action;

import org.javaosc.galaxy.annotation.Autowired;
import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.annotation.Prototype;
import org.javaosc.galaxy.util.PathUtil;
import org.javaosc.galaxy.web.assist.ContextHandler;

import com.javaosc.user.service.UserService;

@Mapping("/user")
@Prototype
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Mapping("/{userId}")
	public void getUserList(User user){
		System.out.println(PathUtil.getClassPath());
		System.out.println(PathUtil.getWebRoot());
		
		user = userService.getUserList(user);
	
		ContextHandler.putJson(user);
	}

}
