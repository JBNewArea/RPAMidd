package com.rpa.task;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.rpa.controller.ProjectController;
import com.rpa.pojo.ProjectDeclare;
import com.rpa.service.IProjectDeclareService;
import com.rpa.service.IUserService;
import com.rpa.utils.ProjectUtils;

public class TimeTask {

	@Resource
	public IUserService userService;
	@Resource
	public IProjectDeclareService projectDeclareService;
	    public void execute()
	    {
	        System.out.println("job2 开始执行"+new Date());
	        ProjectDeclare p = new ProjectDeclare();
	        List<ProjectDeclare> list =  projectDeclareService.queryList(p);
	        String logres = ProjectController.login();
	        if(ProjectUtils.SUCCESS_CODE.equals(logres)){
	        	ProjectController.queryList();
			}
	    }

}
