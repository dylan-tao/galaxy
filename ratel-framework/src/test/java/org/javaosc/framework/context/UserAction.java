package org.javaosc.framework.context;

import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Autowired;
import org.javaosc.framework.annotation.Value;

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
