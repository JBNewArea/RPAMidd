package com.rpa.service.impl;

import javax.annotation.Resource;

import com.rpa.dao.IUserDao;
import com.rpa.pojo.User;
import com.rpa.service.IUserService;

public class UserServiceImpl implements  IUserService{

	@Resource
	private IUserDao userDao;
	
	@Override
	public User getUserById(int userId) {
		return userDao.selectByPrimaryKey(userId);
	}

}
