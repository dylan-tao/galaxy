package com.javaosc.user.action;

import org.javaosc.ratel.annotation.Autowired;
import org.javaosc.ratel.annotation.Mapping;
import org.javaosc.ratel.annotation.Prototype;
import org.javaosc.ratel.util.PathUtil;
import org.javaosc.ratel.web.assist.ContextHandler;

import com.javaosc.user.service.UserService;

@Mapping("/user")
@Prototype
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Mapping("/2")
	public void getUserList(User user){
		System.out.println(PathUtil.getClassPath());
		System.out.println(PathUtil.getWebRoot());
		
		user = userService.getUserList(user);
	
		ContextHandler.putJson(user);
	}

}
