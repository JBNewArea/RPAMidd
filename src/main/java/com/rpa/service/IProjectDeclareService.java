package com.rpa.service;

import java.util.List;

import com.rpa.pojo.ProjectDeclare;

public interface IProjectDeclareService {
	
	public List<ProjectDeclare> queryList(ProjectDeclare dec); 
	public int  insert(ProjectDeclare dec);

}
