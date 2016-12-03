package org.javaosc.framework.context;

import org.javaosc.galaxy.annotation.Autowired;
import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.annotation.Value;

@Mapping("")
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Value("dylan")
	private String userName;
	
	@Mapping("")
	public void get(){
		userService.getUserName();
	}
	
//	public static void main(String[] args) {
//		get();
//	}

}
