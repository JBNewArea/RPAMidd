package com.rpa.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.rpa.utils.CompanyUtils;
import com.rpa.utils.ProjectUtils;
import com.rpa.utils.http.HttpRequest;

import net.sf.json.JSONObject;

public class CompanyTimeTask {

	
	public static void main(String[] args) {
		try {
			String logRes  = login();
			if(CompanyUtils.SUCCESS_CODE.equals(logRes)){
				queryXkz();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 登录
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String login() throws UnsupportedEncodingException{
		String parmas = "iw-apikey="+CompanyUtils.IW_APIKEY+"&iw-cmd="+CompanyUtils.IW_CMD_LOGIN+"&username="+URLEncoder.encode(CompanyUtils.USERNAME, "utf-8")+"&password="+CompanyUtils.PWD;
		try {
			String res = HttpRequest.sendGet(CompanyUtils.SYSLOGIN_URL,parmas);
			JSONObject jsonObject=JSONObject.fromObject(res);
			jsonObject = (JSONObject) jsonObject.get("head");
			System.out.println(jsonObject);
			return String.valueOf(jsonObject.get("rtnCode"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ProjectUtils.ERROR_CODE;
	}
	public static String queryXkz(){
		String params="iw-apikey="+CompanyUtils.IW_APIKEY+"&iw-cmd="+CompanyUtils.IW_CMD_QUERY+"";
		try {
			String res = HttpRequest.sendGet(CompanyUtils.SYSLOGIN_URL,params);
			System.out.println(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
