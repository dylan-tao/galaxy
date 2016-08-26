package com.javaosc.user.service;

import org.javaosc.framework.annotation.Autowired;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.annotation.Value;

import com.javaosc.user.action.User;
import com.javaosc.user.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	
	@Value("123")
	private String password;
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserList(User user) {
		user = userDao.getUser(user);
		user.setPassword(password);
		return user;
	}

}
