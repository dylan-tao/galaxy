package com.javaosc.user.action;

import org.javaosc.framework.annotation.Mapping;

@Mapping("/user")
public class UserAction {
	
	@Mapping("/{userId}")
	public void getUserList(User user){
		System.out.println(user.getUserId());
	}

}
