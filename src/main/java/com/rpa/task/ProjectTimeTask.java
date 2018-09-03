package com.rpa.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.rpa.pojo.Log;
import com.rpa.pojo.LogInfo;
import com.rpa.pojo.ProjectDeclare;
import com.rpa.service.ILogService;
import com.rpa.service.IProjectDeclareService;
import com.rpa.utils.ProjectUtils;
import com.rpa.utils.http.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProjectTimeTask {

	@Resource
	public IProjectDeclareService projectDeclareService;
	@Resource
	public ILogService logService;
	
	private Log log = null;
	private LogInfo logInfo = null;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	
	private SimpleDateFormat projectParams = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	public void execute(){
		System.out.println("进入");
		doLog("project - execute","success:"+df.format(new Date()),"0");
		String logres = login();
		if(ProjectUtils.SUCCESS_CODE.equals(logres)){
			queryList();
		}
		//doDeclare();
	}
	
	
	/**
	 * 登录
	 * @return
	 */
	public  String login(){
		try {
			String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LOGIN+"&username="+ProjectUtils.USERNAME+"&password="+ProjectUtils.PWD;
			String loginRes = HttpRequest.sendGet(ProjectUtils.SYSLOGIN_URL,params);
			JSONObject jsonObject=JSONObject.fromObject(loginRes);
			return String.valueOf(jsonObject.get("rtnCode"));
		} catch (IOException e) {
			doLog("project - login","error:"+e.getMessage(),"1");
			
		}
		return ProjectUtils.ERROR_CODE;
	}
	/**
	 * 遍历得到所有数据
	 */
	@SuppressWarnings("unchecked")
	public  void queryList(){
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LIST+"&page=1";//&datestart="+projectParams.format(new Date())
		try {
			String pageRes = HttpRequest.sendGet(ProjectUtils.LIST_URL, params);
			//得到页数
			JSONObject jsonObject=JSONObject.fromObject(pageRes);
			//判断返回值是否正确
			if(ProjectUtils.SUCCESS_CODE.equals(String.valueOf(jsonObject.get("rtnCode")))){
				JSONObject pageObj = JSONObject.fromObject(jsonObject.get("data"));
				int allPage = Integer.parseInt(String.valueOf(pageObj.get("totlePage")));
				String _res="";
				if(allPage > 0){
					//得到全部的页数，遍历请求地址
					for (int i = 1; i <= allPage; i++) {
						params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LIST+"&page="+i;//&datestart="+projectParams.format(new Date())
						_res = HttpRequest.sendGet(ProjectUtils.LIST_URL, params);
						JSONObject _jsonObject =JSONObject.fromObject(_res);//得到本页列表
						//判断列表不为空
						if(null != _jsonObject){
						    JSONObject _jsonObject1 = JSONObject.fromObject(_jsonObject.get("data"));
						   //判断列表不为空 data
						    if(null != _jsonObject1){
						    	 JSONArray myJsonArray = JSONArray.fromObject(String.valueOf(_jsonObject1.get("proList")));
						    	 Iterator<Object> it = myJsonArray.iterator();
						    	 while (it.hasNext()) {
						    		 JSONObject ob = (JSONObject) it.next();
						    		 if(StringUtils.isNotEmpty(String.valueOf(ob.get("uuid")))&&StringUtils.isNotEmpty(String.valueOf(ob.get("code")))){
						    			 queryDetail(String.valueOf(ob.get("uuid")),String.valueOf(ob.get("code")),String.valueOf(ob.get("sbrq")),String.valueOf(ob.get("xmzt")));
						    		 }
						    	 }
						    }
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
			doLog("project - queryList","error:"+e.getMessage(),"1");
		}
	}
	/**
	 * 详细信息
	 * @param uuid
	 * @param code
	 * @param sbsj
	 */
	public  void queryDetail(String uuid,String code,String sbsj,String xmzt){
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_DETAIL+"&uuid="+uuid+"&code="+code;
		try {
			String res = HttpRequest.sendGet(ProjectUtils.BYONE_URL, params);
			JSONObject jsonObject=JSONObject.fromObject(res);
			if(ProjectUtils.SUCCESS_CODE.equals(jsonObject.get("rtnCode"))){
				  JSONObject _jsonObject=JSONObject.fromObject(jsonObject.get("data"));
				  ProjectDeclare p = new ProjectDeclare();
				  p.setProjectCode(code);
				  List<ProjectDeclare> list =  projectDeclareService.queryList(p);
				  if(list.isEmpty()){
					  p.setId(UUID.randomUUID().toString());
					  p.setDeclareDate(sbsj);
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
					  p.setManageStatus(xmzt);
					  projectDeclareService.insert(p);
				  }
			}
		} catch (IOException e) {
			doLog("project - queryList","error:"+e.getMessage(),"1");
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行 - 全量数据
	 */
	public void doDeclare(){
		ProjectDeclare p = new ProjectDeclare();
		List<Map<String,String>> filelist = doFaceMatter();
		//增加测试时间参数
		SimpleDateFormat _projectParams = new SimpleDateFormat("yyyy/MM/dd");
		p.setDeclareDate(_projectParams.format(new Date()));
		//全量执行
		List<ProjectDeclare> list =  projectDeclareService.queryList(p);
		if(!list.isEmpty()){
			List<NameValuePair> nameValuePairs = null;
			for (ProjectDeclare projectDeclare : list) {
				nameValuePairs = new ArrayList<NameValuePair>();
				if("备案".equals(projectDeclare.getProjectType())&&StringUtils.isNoneEmpty(projectDeclare.getProjectLegalLicenseType())&&!"其他".equals(projectDeclare.getProjectLegalLicenseType())){
					//事项对应附件信息组装
					if(!filelist.isEmpty()){
						for (int i = 0;i<filelist.size();i++) {
							nameValuePairs.add(new BasicNameValuePair("materialList["+i+"].material.id",filelist.get(i).get("id")));
					        nameValuePairs.add(new BasicNameValuePair("materialList["+i+"].material.materialName",filelist.get(i).get("materialName")));
					        nameValuePairs.add(new BasicNameValuePair("materialList["+i+"].materialGetId",""));
					        nameValuePairs.add(new BasicNameValuePair("materialList["+i+"].getMode",""));
						}
					}else{
						nameValuePairs.add(new BasicNameValuePair("materialList[0].material.id",""));
				        nameValuePairs.add(new BasicNameValuePair("materialList[0].material.materialName",""));
				        nameValuePairs.add(new BasicNameValuePair("materialList[0].materialGetId",""));
				        nameValuePairs.add(new BasicNameValuePair("materialList[0].getMode",""));
					}
			        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationName",projectDeclare.getProjectLegalDetp()));
			        nameValuePairs.add(new BasicNameValuePair("applicantDocumentType","2"));
			        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationDocumentNumber",projectDeclare.getProjectLegalLicenseNum()));
			        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationPostCode",""));
			        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationAddress",""));
			        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationPhone",projectDeclare.getLinktel()));
			        nameValuePairs.add(new BasicNameValuePair("applySource","2")); 
			        nameValuePairs.add(new BasicNameValuePair("agentName",""));//当前用户
			        nameValuePairs.add(new BasicNameValuePair("agentDocumentType",""));//代理人证件类型
			        nameValuePairs.add(new BasicNameValuePair("agentDocumentNumber",""));//代理人证件号
			        nameValuePairs.add(new BasicNameValuePair("legalRepresentative",""));
			        nameValuePairs.add(new BasicNameValuePair("status","0"));//代办系统发起的办件 默认状态为已受理 状态码为6
			        nameValuePairs.add(new BasicNameValuePair("express.name",""));
			        nameValuePairs.add(new BasicNameValuePair("express.phone",""));
			        nameValuePairs.add(new BasicNameValuePair("express.postCode",""));
			        nameValuePairs.add(new BasicNameValuePair("express.street",""));
			        nameValuePairs.add(new BasicNameValuePair("express.address",""));
			        nameValuePairs.add(new BasicNameValuePair("projectNumber",projectDeclare.getProjectCode()));
			        nameValuePairs.add(new BasicNameValuePair("concreteTrans",projectDeclare.getProjectName()));
			        //自定义表单组装
			        nameValuePairs.add(new BasicNameValuePair("projectType",projectDeclare.getProjectType()));//项目类型
			        nameValuePairs.add(new BasicNameValuePair("isNationSecurity",""));//是否涉及国家安全
			        nameValuePairs.add(new BasicNameValuePair("projectAttribute",projectDeclare.getProjectAttribute()));//项目属性
			        nameValuePairs.add(new BasicNameValuePair("investmentMethod",""));//投资方式
			        nameValuePairs.add(new BasicNameValuePair("scaleandinfo",projectDeclare.getScaleandinfo()));//项目内容
			        nameValuePairs.add(new BasicNameValuePair("statusFirst",projectDeclare.getManageStatus()));//办理状态
			        nameValuePairs.add(new BasicNameValuePair("buildDetailPlace",projectDeclare.getBuildDetailPlace()));//项目详细地址
			        nameValuePairs.add(new BasicNameValuePair("guanli",projectDeclare.getGuanli()));//所属行业
			        nameValuePairs.add(new BasicNameValuePair("money",projectDeclare.getTotalInvestment()));//总投资
			        nameValuePairs.add(new BasicNameValuePair("dollarMoney","")); //折合美元(万元)
			        nameValuePairs.add(new BasicNameValuePair("dollarProjectCap","")); //折合美元(万元)
			        nameValuePairs.add(new BasicNameValuePair("ExchangeMoney",""));//使用的汇率(人民币/美元)
			        nameValuePairs.add(new BasicNameValuePair("ProjectCap",projectDeclare.getProjectPrincipal()));//项目本金
			        nameValuePairs.add(new BasicNameValuePair("ExchangeProjectCap",""));//使用的汇率(人民币/美元)
			        nameValuePairs.add(new BasicNameValuePair("ProjectCapName",""));//项目资本金投资者名称
			        nameValuePairs.add(new BasicNameValuePair("regCountryArea",""));//注册国别地区
			        nameValuePairs.add(new BasicNameValuePair("contribution",""));//出资额(万元)
			        nameValuePairs.add(new BasicNameValuePair("fundedRatio","")); //出资比例%
			        nameValuePairs.add(new BasicNameValuePair("inveType",""));//出资方式
			        nameValuePairs.add(new BasicNameValuePair("isNewInve",""));//是否涉及新增固定资产投资
			        nameValuePairs.add(new BasicNameValuePair("landMethod","")); //土地获取方式
			        nameValuePairs.add(new BasicNameValuePair("landTotal",""));//总用地面积(平方米)
			        nameValuePairs.add(new BasicNameValuePair("buildTotal",""));//总建筑面积(平方米)
			        nameValuePairs.add(new BasicNameValuePair("expeStratTime",projectDeclare.getStartPlanDate()));//预计开工时间(年)/拟开工时间
			        nameValuePairs.add(new BasicNameValuePair("expeEndTime",projectDeclare.getEndPlanDate()));//预计竣工时间(年)/拟建成时间
			        nameValuePairs.add(new BasicNameValuePair("isNewDevice",""));//是否新增设备
			        nameValuePairs.add(new BasicNameValuePair("impNubAndAmount",""));//其中：拟进口设备数量及金额
			        nameValuePairs.add(new BasicNameValuePair("isUnPrepar",""));//项目单位是否筹建中
			        nameValuePairs.add(new BasicNameValuePair("proCatalogType",""));//项目目录分类
			        nameValuePairs.add(new BasicNameValuePair("recCatalogType",projectDeclare.getPutClassify()));//备案目录分类
			        nameValuePairs.add(new BasicNameValuePair("revCatalogType",""));//审核目录分类
			        nameValuePairs.add(new BasicNameValuePair("assDate",projectDeclare.getGiveDate()));//赋码日期
			        nameValuePairs.add(new BasicNameValuePair("assOffice",projectDeclare.getGiveDept()));//赋码部门
			        nameValuePairs.add(new BasicNameValuePair("guobiao",projectDeclare.getGuobiao()));//国标行业
			        nameValuePairs.add(new BasicNameValuePair("consNature",projectDeclare.getBuildNature()));//建设性质
			        nameValuePairs.add(new BasicNameValuePair("landArea",projectDeclare.getSiteArea()));//用地面积（公顷）
			        nameValuePairs.add(new BasicNameValuePair("addArea",projectDeclare.getNewSiteArea()));//新增用地面积（公顷
			        nameValuePairs.add(new BasicNameValuePair("AgriculturalArea",projectDeclare.getFarmingSiteArea()));//农用地面积（公顷
			        nameValuePairs.add(new BasicNameValuePair("funSocurce",projectDeclare.getCapitalSource()));//资金来源
			        nameValuePairs.add(new BasicNameValuePair("finanFunSocurce",projectDeclare.getFinancialCapitalSource()));//财政资金来源
			        nameValuePairs.add(new BasicNameValuePair("isTechRefPro",projectDeclare.getIsTechnically()));//是否技改项目
			        nameValuePairs.add(new BasicNameValuePair("proNature",""));//项目单位性质
			        nameValuePairs.add(new BasicNameValuePair("proRegAddress",""));//项目单位注册地址
			        nameValuePairs.add(new BasicNameValuePair("businessScope",""));//主要经营范围
			        nameValuePairs.add(new BasicNameValuePair("email",""));//电子邮件
			        nameValuePairs.add(new BasicNameValuePair("fax","")); //传真
					try {
						//事项信息 - 企业投资建设固定资产投资项目备案
						if(ProjectUtils.ISTECHNICALLY.equals(projectDeclare.getIsTechnically())){
							 nameValuePairs.add(new BasicNameValuePair("trans.id","e8f74efb01ce443faf91f77c064ad781"));
							 Map<String,Object> ret = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
						doLog("project - doDeclare","error:"+e.getMessage(),"1");
					}
				}
			}
		}
	}
	
	/**
	 * 接口 - 得到对应事项 的所有材料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String,String>> doFaceMatter(){
		List<Map<String,String>> list = new ArrayList<>();
		Map<String,String> _temp = null;
		try {
			String res = HttpRequest.sendPost(ProjectUtils.MATTERINFO_URL,"transBaseCode=1000001000-1");
			if(StringUtils.isNotEmpty(res)){
				JSONArray myJsonArray = JSONArray.fromObject(res);
				Iterator<Object> it = myJsonArray.iterator();
		    	 while (it.hasNext()) {
		    		 JSONObject ob = (JSONObject) it.next();
		    		 String matterDe = HttpRequest.sendPost(ProjectUtils.INTERFACEURL+ProjectUtils.MATTERINFO_ACTIONNAMEURL,"id="+ob.get("id"));
		    		 if(StringUtils.isNotEmpty(matterDe)){
		    			 JSONObject _matterDeJsonObj=JSONObject.fromObject(matterDe);
		    			 String matterFiles = HttpRequest.sendPost(ProjectUtils.INTERFACEURL+ProjectUtils.MATTERFILEURL,"ids="+_matterDeJsonObj.get("materialIds"));
		    			 System.out.println(matterFiles);
		    			 if(StringUtils.isNotEmpty(matterFiles)){
		    				 JSONArray matterFileArr = JSONArray.fromObject(matterFiles);
		    				 it = matterFileArr.iterator();
		    				 while (it.hasNext()) {
		    					 ob = (JSONObject) it.next();
		    					 _temp = new HashMap<>();
		    					 _temp.put("id",ob.getString("id"));
		    					 _temp.put("materialName",ob.getString("materialName"));
		    					 list.add(_temp);
		    				 }
		    			 }
		    		 }
		    	 }
			}
		} catch (IOException e) {
			doLog("project - doFaceMatter","error:"+e.getMessage(),"1");
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 记录日志信息
	 * @param actionName
	 * @param error
	 * @param status
	 */
	public void doLog(String actionName,String error,String status){
		log = new Log();
		logInfo = new LogInfo();
		String logId = UUID.randomUUID().toString();
		log.setId(logId);
		log.setActionName(actionName);
		log.setCreateTime(df.format(new Date()));
		logInfo.setId(UUID.randomUUID().toString());
		logInfo.setLogId(logId);
		logInfo.setStatus(status);
		logInfo.setContent(error);
		logInfo.setCreateTime(df.format(new Date()));
		logService.record(log, logInfo);
	}
}
