package com.rpa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rpa.dao.IProjectDeclareDao;
import com.rpa.pojo.ProjectDeclare;
import com.rpa.service.IProjectDeclareService;

@Service("projectDeclareService")
public class ProjectDeclareServiceImpl implements IProjectDeclareService{
	@Resource
	private IProjectDeclareDao projectDeclareDao;
	@Override
	public List<ProjectDeclare> queryList(ProjectDeclare dec) {
		return projectDeclareDao.queryList(dec);
	}
	@Override
	public int insert(ProjectDeclare dec) {
		return projectDeclareDao.insert(dec);
	}
	@Override
	public int updateBjbh(ProjectDeclare dec) {
		return projectDeclareDao.updateBjbh(dec);
	}

}
