package com.rpa.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping
public class projectServices {

	@ResponseBody
	@RequestMapping(value = "/doFirst", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public String doFirst(@PathVariable("userName") String userName){
		System.out.println(userName);
		return userName;
	}
}
