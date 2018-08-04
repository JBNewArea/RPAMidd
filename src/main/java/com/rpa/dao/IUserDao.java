package com.rpa.dao;

import com.rpa.pojo.User;

public interface IUserDao {
	
	public User selectByPrimaryKey(String userid);
}
