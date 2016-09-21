package io.spring.user.service;

import io.spring.user.action.User;
import io.spring.user.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Value("#{config['javaosc.password.value']}")
	private String password;
	
	@Override
	public User getUserList(User user) {
		user = userDao.getUser(user);
		user.setPassword(password);
		return user;
	}

}
