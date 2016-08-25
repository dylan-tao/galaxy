package com.javaosc.user.dao;

import org.javaosc.framework.annotation.Dao;
import org.javaosc.framework.annotation.Ref;
import org.javaosc.framework.jdbc.JdbcHandler;

import com.javaosc.user.action.User;

@Dao
public class UserDao {
	
	@Ref
	private JdbcHandler jdbcHandler;
	
	public User getUser(User user){
		String sql = "select * from user where user_id = ?";
		return jdbcHandler.getForObject(sql, User.class, user.getUserId());
	}
}
