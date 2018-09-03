package com.rpa.service;
import com.rpa.pojo.Log;
import com.rpa.pojo.LogInfo;
public interface ILogService {

	public int record(Log log,LogInfo logInfo);
}
