package org.javaosc.framework.context;

import org.javaosc.galaxy.annotation.Service;
import org.javaosc.galaxy.annotation.Value;

@Service
public class UserServiceImpl implements UserService{
	
	@Value("dylan")
	private String userName;
	
	@Override
	public String getUserName() {
		return userName;
	}

}
