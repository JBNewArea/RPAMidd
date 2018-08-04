package com.rpa.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.rpa.dao.IUserDao;
import com.rpa.pojo.User;
import com.rpa.service.IUserService;

@Service("userService")
public class UserServiceImpl implements  IUserService{

	@Resource
	private IUserDao userDao;
	
	@Override
	public User getUserById(String userId) {
		return userDao.selectByPrimaryKey(userId);
	}

}
