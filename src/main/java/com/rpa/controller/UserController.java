package com.rpa.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rpa.pojo.User;
import com.rpa.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	public IUserService userService;
	
	@RequestMapping("/showUser")
	public void toIndex(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.getUserById(userId);
		System.out.println(user.getAge());
		 
	}
}
