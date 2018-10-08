package com.rpa.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rpa.pojo.ProjectDeclare;
import com.rpa.utils.ProjectUtils;
import com.rpa.utils.http.HttpRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	private static Logger logger = Logger.getLogger(ProjectController.class);
	
	
	public static void main(String[] args) {
		String logres = login();
		if(ProjectUtils.SUCCESS_CODE.equals(logres)){
			queryList();
		}
	}
	
	/**
	 * 登录
	 * @return
	 */
	public static String login(){
		try {
			String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LOGIN+"&username="+ProjectUtils.USERNAME+"&password="+ProjectUtils.PWD;
			String loginRes = HttpRequest.sendGet(ProjectUtils.SYSLOGIN_URL,params);
			JSONObject jsonObject=JSONObject.fromObject(loginRes);
			return String.valueOf(jsonObject.get("rtnCode"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ProjectUtils.ERROR_CODE;
	}
	
	/**
	 * 
	 */
	public static void queryList(){
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LIST+"&page=1";
		try {
			String pageRes = HttpRequest.sendGet(ProjectUtils.LIST_URL, params);
			//得到页数
			JSONObject jsonObject=JSONObject.fromObject(pageRes);
			//判断返回值是否正确
			if(ProjectUtils.SUCCESS_CODE.equals(String.valueOf(jsonObject.get("rtnCode")))){
				JSONObject pageObj = JSONObject.fromObject(jsonObject.get("data"));
				int allPage = Integer.parseInt(String.valueOf(pageObj.get("totlePage")));
				System.out.println(allPage);
				String _res="";
				if(allPage > 0){
					//得到全部的页数，遍历请求地址
					for (int i = 1; i <= 1; i++) {
						params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LIST+"&page="+i+"&datestart=2018-08-29";
						_res = HttpRequest.sendGet(ProjectUtils.LIST_URL, params);
						JSONObject _jsonObject =JSONObject.fromObject(_res);//得到本页列表
						//判断列表不为空
						if(null != _jsonObject){
						    JSONObject _jsonObject1 = JSONObject.fromObject(_jsonObject.get("data"));
						    System.out.println(_jsonObject1.get("proList"));
						   //判断列表不为空 data
						    if(null != _jsonObject1){
						    	 JSONArray myJsonArray = JSONArray.fromObject(String.valueOf(_jsonObject1.get("proList")));
						    	 Iterator<Object> it = myJsonArray.iterator();
						    	 while (it.hasNext()) {
						    		 JSONObject ob = (JSONObject) it.next();
						    		 if(StringUtils.isNotEmpty(String.valueOf(ob.get("uuid")))&&StringUtils.isNotEmpty(String.valueOf(ob.get("code")))){
						    			 queryDetail(String.valueOf(ob.get("uuid")),String.valueOf(ob.get("code")));
						    		 }
						    	 }
						    }
						}
					}
				}
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void queryDetail(String uuid,String code){
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_DETAIL+"&uuid="+uuid+"&code="+code;
		try {
			String res = HttpRequest.sendGet(ProjectUtils.BYONE_URL, params);
			JSONObject jsonObject=JSONObject.fromObject(res);
			System.out.println(jsonObject);
			if(ProjectUtils.SUCCESS_CODE.equals(jsonObject.get("rtnCode"))){
				  JSONObject _jsonObject=JSONObject.fromObject(jsonObject.get("data"));
				  ProjectDeclare p = new ProjectDeclare();
				  p.setId(UUID.randomUUID().toString());
				  p.setProjectType(_jsonObject.getString("xmlx"));
				  p.setProjectName(_jsonObject.getString("xmmc"));
				  p.setMainProjectName(_jsonObject.getString("zxmmc"));
				  p.setGuanli(_jsonObject.getString("glhy"));
				  p.setGiveDate(_jsonObject.getString("fmrq"));
				  p.setGiveDept(_jsonObject.getString("fmbm"));
				  p.setStartPlanDate(_jsonObject.getString("nkgsj"));
				  p.setEndPlanDate(_jsonObject.getString("njcsj"));
				  p.setBuildPlace(_jsonObject.getString("jsdd"));
				  p.setGuobiao(_jsonObject.getString("gbhy"));
				  p.setBuildDetailPlace(_jsonObject.getString("xmxxdz"));
				  p.setProjectAttribute(_jsonObject.getString("xmsx"));
				  p.setBuildNature(_jsonObject.getString("jsxz"));
				  p.setTotalInvestment(_jsonObject.getString("ztz"));
				  p.setBuildDetailPlace(_jsonObject.getString("jsgmjnr"));
				  p.setSiteArea(_jsonObject.getString("ydmj"));
				  p.setNewSiteArea(_jsonObject.getString("xzydmj"));
				  p.setFarmingSiteArea(_jsonObject.getString("nydmj"));
				  p.setProjectPrincipal(_jsonObject.getString("xmzbj"));
				  p.setCapitalSource(_jsonObject.getString("zjly"));
				  p.setFinancialCapitalSource(_jsonObject.getString("czzjly"));
				  p.setIsTechnically(_jsonObject.getString("sfjgxm"));
				  p.setPutClassify(_jsonObject.getString("bamlfl"));
				  p.setPutCatalog(_jsonObject.getString("baml"));
				  p.setProjectLegalDetp(_jsonObject.getString("xmfrdw"));
				  p.setProjectLegalLicenseType(_jsonObject.getString("xmfrzzlx"));
				  p.setProjectLegalLicenseNum(_jsonObject.getString("xmfrzzhm"));
				  p.setLinkman(_jsonObject.getString("lxr"));
				  p.setLinktel(_jsonObject.getString("lxdh"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
