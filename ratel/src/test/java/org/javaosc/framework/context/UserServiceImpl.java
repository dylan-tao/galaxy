package org.javaosc.framework.context;

import org.javaosc.ratel.annotation.Service;
import org.javaosc.ratel.annotation.Value;

@Service
public class UserServiceImpl implements UserService{
	
	@Value("dylan")
	private String userName;
	
	@Override
	public String getUserName() {
		return userName;
	}

}
