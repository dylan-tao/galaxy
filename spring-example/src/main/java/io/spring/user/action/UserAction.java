package io.spring.user.action;

import io.spring.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserAction {

	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/{userId}")
	public User getUserList(User user){
		return userService.getUserList(user);
	}
}
