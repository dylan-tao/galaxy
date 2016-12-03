package com.javaosc.user.dao;

import org.javaosc.galaxy.annotation.Autowired;
import org.javaosc.galaxy.annotation.Dao;
import org.javaosc.galaxy.jdbc.JdbcTemplate;

import com.javaosc.user.action.User;

@Dao
public class UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User getUser(User user){
		String sql = "select user_id,user_name,password from user where user_id = ?";
		return jdbcTemplate.getForObject(sql, User.class, user.getUserId());
	}
}
