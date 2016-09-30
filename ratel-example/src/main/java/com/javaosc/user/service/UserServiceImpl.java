package com.javaosc.user.service;

import org.javaosc.ratel.annotation.Autowired;
import org.javaosc.ratel.annotation.Service;
import org.javaosc.ratel.annotation.Value;

import com.javaosc.user.action.User;
import com.javaosc.user.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	
	@Value("javaosc.password.value")
	private String password;
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserList(User user) {
		user = userDao.getUser(user);
		if(user!=null){
			user.setPassword(password);
		}
		return user;
	}

}
