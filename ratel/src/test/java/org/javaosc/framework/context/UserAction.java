package org.javaosc.framework.context;

import org.javaosc.ratel.annotation.Autowired;
import org.javaosc.ratel.annotation.Mapping;
import org.javaosc.ratel.annotation.Value;

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
