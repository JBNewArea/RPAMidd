package com.rpa.task;

import java.util.Date;

import javax.annotation.Resource;

import com.rpa.pojo.User;
import com.rpa.service.IUserService;

public class TimeTask {

	@Resource
	public IUserService userService;
	    public void execute()
	    {
	        System.out.println("job2 开始执行"+new Date());
	        User user = this.userService.getUserById("");
			System.out.println(user.getAge());
	    }

}
