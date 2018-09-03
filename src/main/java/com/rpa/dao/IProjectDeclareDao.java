package com.rpa.dao;

import java.util.List;

import com.rpa.pojo.ProjectDeclare;

public interface IProjectDeclareDao {
	
	public List<ProjectDeclare> queryList(ProjectDeclare dec);
	
	public int  insert(ProjectDeclare dec);

}
