package io.spring.user.dao;

import io.spring.common.dao.JdbcHandler;
import io.spring.user.action.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	
	@Autowired
	private JdbcHandler jdbcHandler;
	
	public User getUser(User user){
		String sql = "select user_id,user_name,password from user where user_id = ?";
		return jdbcHandler.getForObject(sql, User.class, user.getUserId());
	}
}
