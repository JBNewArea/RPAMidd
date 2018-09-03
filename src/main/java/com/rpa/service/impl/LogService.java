package com.rpa.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.rpa.dao.ILogDao;
import com.rpa.dao.ILogInfoDao;
import com.rpa.pojo.Log;
import com.rpa.pojo.LogInfo;
import com.rpa.service.ILogService;

@Service("logService")
public class LogService implements ILogService {
	@Resource
	private ILogDao logDao;
	@Resource
	private ILogInfoDao logInfoDao;
	@Override
	public int record(Log log, LogInfo logInfo) {
		int ret = logDao.inser(log);
		if(ret>0){
			ret = logInfoDao.insert(logInfo);
		}
		return ret;
	}
}
