package com.rpa.task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.management.StringValueExp;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.rpa.pojo.ProjectDeclare;
import com.rpa.service.ILogService;
import com.rpa.service.IProjectDeclareService;
import com.rpa.utils.CompanyUtils;
import com.rpa.utils.ProjectUtils;
import com.rpa.utils.http.HttpRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 备案项目预审
 * @author Arthur
 *
 */
public class ProjectPreliminaryTask {
	@Resource
	public ILogService logService;
	@Resource
	public IProjectDeclareService projectDeclareService;
	
	private List<ProjectDeclare> project = new ArrayList<>();//系统缓存
	public void execute(){
//		String logRes  = login();
//		if(CompanyUtils.SUCCESS_CODE.equals(logRes)){
//			pretralList();
//		}
		doDeclare();
	}
	
	/**
	 * 模拟 - 登录
	 * @return
	 */
	public   String login(){
		try {
			String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_LOGIN+"&username="+ProjectUtils.USERNAME_SP+"&password="+ProjectUtils.PWD_SP;
			String loginRes = HttpRequest.sendGet(ProjectUtils.SYSLOGIN_URL,params);
			System.out.println(loginRes);
			JSONObject jsonObject=JSONObject.fromObject(loginRes);
			return String.valueOf(jsonObject.get("rtnCode"));
		} catch (IOException e) {
		}
		return ProjectUtils.ERROR_CODE;
	}
	
	/**
	 * 待预审列表
	 */
	public  void pretralList(){
		//组装配置请求参数
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_PRETRIAL+"&page=1";
		try {
			//发起get请求
			String pageRes = HttpRequest.sendGet(ProjectUtils.PRELIMINARY_URL, params);
			//得到页数
			JSONObject jsonObject=JSONObject.fromObject(pageRes);
			//判断返回值是否正确
			if(ProjectUtils.SUCCESS_CODE.equals(String.valueOf(jsonObject.get("rtnCode")))){
				//得到列表中集合参数
				JSONObject pageObj = JSONObject.fromObject(jsonObject.get("data"));
				//得到所有的页数
				int allPage = Integer.parseInt(String.valueOf(pageObj.get("totlePage")));
				String _res="";
				//判断页数不为0，判断存在数据
				if(allPage > 0){
					//得到全部的页数，遍历请求每页的地址
					for (int i = 1; i <= allPage; i++) {
						//页数大于0，模拟人工翻页得到每一页的数据
						params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_PRETRIAL+"&page="+i;
						//请求
						_res = HttpRequest.sendGet(ProjectUtils.PRELIMINARY_URL, params);
						//得到本页列表数据
						JSONObject _jsonObject =JSONObject.fromObject(_res);
						//判断列表不为空
						if(null != _jsonObject&&ProjectUtils.SUCCESS_CODE.equals(_jsonObject.get("rtnCode"))){
							//从列表数据中得到对应的每集合实体
							JSONObject _jsonObject1 = JSONObject.fromObject(_jsonObject.get("data"));
							//判断列表不为空 data
						    if(null != _jsonObject1){
						    	 //分解array中的数据
						    	 JSONArray myJsonArray = JSONArray.fromObject(String.valueOf(_jsonObject1.get("proList")));
						    	 //循环每页数据
						    	 Iterator<Object> it = myJsonArray.iterator();
						    	 //循环数据下一个
						    	 while (it.hasNext()) {
						    		 //得到独立的实体、传入主健编号
						    		 JSONObject ob = (JSONObject) it.next();
						    		 queryDetail(String.valueOf(ob.get("projectUuid")),String.valueOf(ob.get("projectCode")),String.valueOf(ob.get("isForeign")));
						    	 }
						    }
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public  void queryDetail(String projectUuid,String projectCode_lj,String isForeign){
		//参数
		String params="iw-apikey="+ProjectUtils.IW_APIKEY+"&iw-cmd="+ProjectUtils.IW_CMD_PREDETAIL+"&projectUuid="+projectUuid+"&isForeign="+isForeign;
		ProjectDeclare p = null;
		try {
			 p = new ProjectDeclare();
			 p.setProjectUuid(projectUuid);
			 List<ProjectDeclare> list =  projectDeclareService.queryList(p);
			 if(list.isEmpty()){
				//组装 - 请求
					String res = HttpRequest.sendGet(ProjectUtils.PRELIMINARY_URL, params);
					JSONObject jsonObject=JSONObject.fromObject(res);
					System.out.println(jsonObject);
					//验证返回格式是否正常
					if(ProjectUtils.SUCCESS_CODE.equals(jsonObject.get("rtnCode"))){
						 //从返回格式中得到普通的数据
						 JSONObject _jsonObject=JSONObject.fromObject(jsonObject.get("data"));
						  if(null != _jsonObject){
							      //内资
							      if("0".equals(isForeign)){
							    	  _jsonObject=JSONObject.fromObject( _jsonObject.get("nzInfo"));
								      p.setId(UUID.randomUUID().toString());
									  p.setProjectUuid(projectUuid);
									  p.setProjectSplx(_jsonObject.getString("xmsplx"));//项目审批类型
									  p.setPutCatalog(_jsonObject.getString("baml"));//备案目录
									  p.setProjectName(_jsonObject.getString("xmmc"));//项目名称
									  p.setMainProjectName(_jsonObject.getString("zxmmc"));//主项目名称
									  p.setSfbbxm("否");//是否补办项目_jsonObject.getString("sfbbxm")
									  p.setLxrq(_jsonObject.getString("lxrq"));//立项日期
									  p.setProjectCode(_jsonObject.getString("xmdm"));//项目代码
									  p.setDeclareDate(_jsonObject.getString("sbsj"));//申报时间
									  p.setProjectType(_jsonObject.getString("xmlx"));//项目类型
									  p.setPutClassify(_jsonObject.getString("bamlfl"));//备案目录分类
									  p.setBuildNature(_jsonObject.getString("jsxz"));//建设性质
									  p.setProjectAttribute(_jsonObject.getString("xmsx"));//项目属性
									  p.setStartPlanDate(_jsonObject.getString("nkgsj"));//拟开工时间
									  p.setEndPlanDate(_jsonObject.getString("njcsj"));//拟建成时间
									  p.setBuildPlace(_jsonObject.getString("jsdd"));//建设地点
									  p.setGuobiao(_jsonObject.getString("gbhy"));//国标行业
									  p.setGuanli(_jsonObject.getString("glhy"));//管理行业
									  p.setBuildDetailPlace(_jsonObject.getString("xmxxdz"));//项目详细地址
									  p.setScaleandinfo(_jsonObject.getString("jsgmjnr"));//建设规模
									  p.setTotalInvestment(_jsonObject.getString("ztz"));//总投资
									  p.setSumExplain(_jsonObject.getString("ztzsm"));//总投资说明
									  p.setSiteArea(_jsonObject.getString("ydmj"));//用地面积
									  p.setNewSiteArea(_jsonObject.getString("xzydmj"));//新增用地面积
									  p.setFarmingSiteArea(_jsonObject.getString("nyydmj"));//农用地面积
									  p.setProjectCapital(_jsonObject.getString("xmzbj"));//项目资本金
									  p.setCapitalSource(_jsonObject.getString("zjly"));//资金来源
									  p.setFinancialCapitalSource(_jsonObject.getString("czzjly"));//财政资金来源
									  p.setIsTechnically(_jsonObject.getString("sfjgxm"));//是否技改
									  p.setPolicyTypes(_jsonObject.getString("cyzclx"));//产业政策类型
									  p.setIndustrialStructure(_jsonObject.getString("cyjgtz"));//产业结构调整指导目录
									  p.setProjectLegalDetp( _jsonObject.getString("xmfrdw"));//项目法人单位
									  p.setEconomicType(_jsonObject.getString("frjjlx"));//法人经济类型
									  p.setProjectLegalLicenseType(_jsonObject.getString("frzzlx"));//法人证照类型
									  p.setProjectLegalLicenseNum(_jsonObject.getString("frzzhm"));//法人证照号码
									  p.setLinkman(_jsonObject.getString("dwlxr"));//法人联系人
									  p.setLinktel(_jsonObject.getString("sjhm"));//联系方式
									  p.setEmail(_jsonObject.getString("dzyx"));//电子邮箱
									  p.setApplicationUnit(_jsonObject.getString("xmsbdw"));//项目申报单位
									  p.setApplicationeConomicType(_jsonObject.getString("sbjjlx"));//项目申报类型
									  p.setApplicationeLinceType(_jsonObject.getString("xmzzlx"));//项目申报证照类型
									  p.setApplicationeLinceNum(_jsonObject.getString("xmzzhm"));//项目申报证照号码
									  p.setApplicationeLinkMan(_jsonObject.getString("sbdwlxr"));//项目单位联系人
									  p.setApplicationeLinkTel(_jsonObject.getString("sjhm1"));//项目申报联系人电话
									  p.setApplicationeEmail(_jsonObject.getString("dzyx1"));//项目申报电子邮箱
									  p.setProjectCode_lj(projectCode_lj);//项目代码 - 逻辑判断
									  p.setIsForeign(isForeign);//内外资 辨别 0内资 1外资
							      }
							      //外资
							      else if ("1".equals(isForeign)){
							    	  _jsonObject=JSONObject.fromObject( _jsonObject.get("wzInfo"));
								      p.setId(UUID.randomUUID().toString());
									  p.setProjectUuid(projectUuid);
									  p.setProjectSplx(_jsonObject.getString("xmsplx"));//项目审批类型
									  p.setProjectType(_jsonObject.getString("xmlx"));//项目类型
									  p.setPutCatalog(_jsonObject.getString("baml"));//备案目录
									  p.setProjectName(_jsonObject.getString("xmmc"));//项目名称
									  p.setSfbbxm("否");//是否补办项目_jsonObject.getString("sfbbxm")
									  p.setLxrq(_jsonObject.getString("lxrq"));//立项日期
									  p.setIsNationalSecurity(_jsonObject.getString("sfsjgjaq"));//是否涉及国家安全
									  //p.setAqscjdwh(_jsonObject.getString(""));//安全审查决定文号
									  p.setIsTechnically(_jsonObject.getString("sfjg"));//是否技改
									  p.setProjectAttribute(_jsonObject.getString("xmsx"));//项目属性
									  p.setInvestmentWay(_jsonObject.getString("tzfs"));//投资方式
									  p.setScaleandinfo(_jsonObject.getString("xmnr"));//建设规模&项目内容
									  p.setPolicyTypes(_jsonObject.getString("sycyzctmlx"));//产业政策类型
									  p.setIndustrialStructure(_jsonObject.getString("sycyzctm"));//产业结构调整指导目录
									  p.setGuobiao(_jsonObject.getString("gbhy"));//国标行业
									  p.setIndustryInvolved(_jsonObject.getString("sshy"));//所属行业
									  p.setBuildPlace(_jsonObject.getString("xmdz"));//建设地点
									  p.setBuildDetailPlace(_jsonObject.getString("xmxxdz"));//项目详细地址
									  p.setTotalInvestment(_jsonObject.getString("ztz"));//总投资
									  p.setTotalConvertDollar(_jsonObject.getString("zhmy"));// 总投资 - 折合美元
									  p.setTotalParitiesRMBorDollar(_jsonObject.getString("syhl"));//总投资 - 使用汇率
									  p.setProjectCapital(_jsonObject.getString("xmzbj"));//项目资本金
									  p.setCapitalConvertDollar(_jsonObject.getString("zhmy1"));//项目资本金- 折合美元
									  p.setCapitalParitiesRMBORDollar(_jsonObject.getString("syhl1"));//项目资本金 - 使用汇率
									  /**
									   * 投资者名称-存在多个目前审批系统接口只能传一个参数 
									   */
									  //分解array中的数据
								    	 JSONArray myJsonArray = JSONArray.fromObject(String.valueOf(_jsonObject.get("tzzxx")));
								    	 //循环每页数据
								    	 Iterator<Object> it = myJsonArray.iterator();
								    	 //循环数据下一个
								    	 while (it.hasNext()) {
								    		//得到独立的实体、传入主健编号
								    		 JSONObject ob = (JSONObject) it.next();
								    		 p.setInvestorName(ob.getString("tzzmc"));
								    		 p.setRegisterStateArea(ob.getString("zcgbdq"));
								    		 p.setContributiveLimit(ob.getString("cze"));
								    		 p.setRatioOfInvestments(ob.getString("czbl"));
								    		 p.setWaysOfInvestment(ob.getString("czfs"));
								    	 }
								    
								    	 p.setNewFixedAssets(_jsonObject.getString("sfsjxzgdzctz"));//是否涉及新增固定资产
								    	 p.setLandAcquisition(_jsonObject.getString("tdhqfs"));//土地获取方式
								    	 p.setSumSiteArea(_jsonObject.getString("zydmj"));//总用地面积
								    	 p.setSumCoveredArea(_jsonObject.getString("zjzmj"));//总建筑面积
								    	 p.setStartPlanDate(_jsonObject.getString("yjkgsj"));//拟开工时间
										 p.setEndPlanDate(_jsonObject.getString("yjjgsj"));//拟建成时间
										 p.setIsAddEquipment(_jsonObject.getString("sfxzsb"));//是否新增设备
										 p.setImportedEquipmentAndMoney(_jsonObject.getString("njkslhje"));//其中：拟进口设备数量及金
										 p.setApplicationUnit(_jsonObject.getString("xmfrdw"));//项目申报单位
										 p.setEconomicType(_jsonObject.getString("frjjlx"));//经济类型
										 p.setApplicationeLinceType(_jsonObject.getString("frzzlx"));//项目申报证照类型
										 p.setApplicationeLinceNum(_jsonObject.getString("frzzhm"));//项目申报证照号码
										 p.setApplicationeLinkMan(_jsonObject.getString("dwlxr"));//项目单位联系人
										 p.setApplicationeLinkTel(_jsonObject.getString("sjhm"));//项目申报联系人电话
										 p.setApplicationeEmail(_jsonObject.getString("dzyx"));//项目申报电子邮箱
									     p.setProjectCode_lj(projectCode_lj);//项目代码 - 逻辑判断
									     p.setIsForeign(isForeign);//内外资 辨别 0内资 1外资
							      }
						  }
						  int ret = projectDeclareService.insert(p);
						  if(ret > 0 ){
							  project.add(p);
						  }
						  //得到附件数据
//						  JSONArray myJsonArray = JSONArray.fromObject(String.valueOf(_jsonObject.get("fj")));
//						  if(myJsonArray.size() >0){
//							  for (int i = 0; i < myJsonArray.size(); i++) {
//								System.out.println(myJsonArray.get(i));
//								//ProjectUtils.downloadNet(String.valueOf(myJsonArray.get(i)));
//							  }
//						 }
					}
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行 - 全量数据
	 */
	public void doDeclare(){
		List<Map<String,String>> filelist = doFaceMatter();
		 ProjectDeclare p = new ProjectDeclare();
		 p.setProjectUuid("14cb90bce2d8480bb4fdfbd7d4e472f3");
		 List<ProjectDeclare> list =  projectDeclareService.queryList(p);
		//全量执行
		if(!list.isEmpty()){
			List<NameValuePair> nameValuePairs = null;
			for (int a = 0; a < list.size(); a++) {
				ProjectDeclare projectDeclare = list.get(a);
						nameValuePairs = new ArrayList<NameValuePair>();
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
						/**
						 * 投资类型 
						 */
						if("0".equals(projectDeclare.getIsForeign())){
							 nameValuePairs.add(new BasicNameValuePair("neiWaiType","2"));//外商投资类型 1 - 外资 2 - 内资
							 nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationDocumentNumber",projectDeclare.getProjectLegalLicenseNum()));
							 nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationName",projectDeclare.getProjectLegalDetp()));
							 nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationPhone",projectDeclare.getLinktel()));
							 nameValuePairs.add(new BasicNameValuePair("projectTypeNei",StringUtils.isEmpty(projectDeclare.getProjectType())?"":projectDeclare.getProjectType()));//项目类型
							 nameValuePairs.add(new BasicNameValuePair("scaleandinfoNei",StringUtils.isEmpty(projectDeclare.getScaleandinfo())?"":projectDeclare.getScaleandinfo()));//项目内容
							 nameValuePairs.add(new BasicNameValuePair("projectAttributeNei",StringUtils.isEmpty(projectDeclare.getProjectAttribute())?"":projectDeclare.getProjectAttribute()));//项目属性
							 nameValuePairs.add(new BasicNameValuePair("guobiaoNei",StringUtils.isEmpty(projectDeclare.getGuobiao())?"":projectDeclare.getGuobiao()));//国标行业
							 nameValuePairs.add(new BasicNameValuePair("guanliNei",StringUtils.isEmpty(projectDeclare.getGuanli())?"":projectDeclare.getGuanli()));//所属行业
							 nameValuePairs.add(new BasicNameValuePair("buildDetailPlaceNei",StringUtils.isEmpty(projectDeclare.getBuildDetailPlace())?"":projectDeclare.getBuildDetailPlace()));//项目详细地址
							 nameValuePairs.add(new BasicNameValuePair("moneyNei",StringUtils.isEmpty(projectDeclare.getTotalInvestment())?"":projectDeclare.getTotalInvestment()));//总投资
							 nameValuePairs.add(new BasicNameValuePair("expeStratTimeNei",StringUtils.isEmpty(projectDeclare.getStartPlanDate())?"":projectDeclare.getStartPlanDate()));//预计开工时间(年)/拟开工时间
						        nameValuePairs.add(new BasicNameValuePair("expeEndTimeNei",StringUtils.isEmpty(projectDeclare.getEndPlanDate())?"":projectDeclare.getEndPlanDate()));//预计竣工时间(年)/拟建成时间
						}else if("1".equals(projectDeclare.getIsForeign())){
							nameValuePairs.add(new BasicNameValuePair("neiWaiType","1"));//外商投资类型 1 - 外资 2 - 内资
							nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationDocumentNumber",projectDeclare.getApplicationeLinkTel()));
							nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationName",projectDeclare.getApplicationeLinkMan()));
							nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationPhone",projectDeclare.getApplicationeLinkTel()));
							nameValuePairs.add(new BasicNameValuePair("projectType",StringUtils.isEmpty(projectDeclare.getProjectType())?"":projectDeclare.getProjectType()));//项目类型
							nameValuePairs.add(new BasicNameValuePair("scaleandinfo",StringUtils.isEmpty(projectDeclare.getScaleandinfo())?"":projectDeclare.getScaleandinfo()));//项目内容
							nameValuePairs.add(new BasicNameValuePair("projectAttribute",StringUtils.isEmpty(projectDeclare.getProjectAttribute())?"":projectDeclare.getProjectAttribute()));//项目属性
							nameValuePairs.add(new BasicNameValuePair("guobiao",StringUtils.isEmpty(projectDeclare.getGuobiao())?"":projectDeclare.getGuobiao()));//国标行业
							nameValuePairs.add(new BasicNameValuePair("guanli",StringUtils.isEmpty(projectDeclare.getGuanli())?"":projectDeclare.getGuanli()));//所属行业
							nameValuePairs.add(new BasicNameValuePair("buildDetailPlace",StringUtils.isEmpty(projectDeclare.getBuildDetailPlace())?"":projectDeclare.getBuildDetailPlace()));//项目详细地址
							nameValuePairs.add(new BasicNameValuePair("money",StringUtils.isEmpty(projectDeclare.getTotalInvestment())?"":projectDeclare.getTotalInvestment()));//总投资
							nameValuePairs.add(new BasicNameValuePair("expeStratTime",StringUtils.isEmpty(projectDeclare.getStartPlanDate())?"":projectDeclare.getStartPlanDate()));//预计开工时间(年)/拟开工时间
						    nameValuePairs.add(new BasicNameValuePair("expeEndTime",StringUtils.isEmpty(projectDeclare.getEndPlanDate())?"":projectDeclare.getEndPlanDate()));//预计竣工时间(年)/拟建成时间
						}
						nameValuePairs.add(new BasicNameValuePair("projecCode",StringUtils.isEmpty(projectDeclare.getProjectCode())?"":projectDeclare.getProjectCode())); //项目代码
						nameValuePairs.add(new BasicNameValuePair("isBubanpro",projectDeclare.getSfbbxm()));//是否补办项目
						nameValuePairs.add(new BasicNameValuePair("prjectID",projectDeclare.getProjectUuid()+","+projectDeclare.getProjectCode_lj()));//投资项目ID标识
				        nameValuePairs.add(new BasicNameValuePair("applicantDocumentType","2"));
				        
				        
				        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationPostCode",""));
				        nameValuePairs.add(new BasicNameValuePair("applyPerson.applicationAddress",""));
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
				        nameValuePairs.add(new BasicNameValuePair("projectNumber",""));
				        nameValuePairs.add(new BasicNameValuePair("concreteTrans",StringUtils.isEmpty(projectDeclare.getProjectName())?"":projectDeclare.getProjectName()));
				        //自定义表单组装 
				         nameValuePairs.add(new BasicNameValuePair("isNationSecurity",StringUtils.isEmpty(projectDeclare.getIsNationalSecurity())?"":projectDeclare.getIsNationalSecurity()));//是否涉及国家安全
				        nameValuePairs.add(new BasicNameValuePair("investmentMethod",StringUtils.isEmpty(projectDeclare.getInvestmentWay())?"":projectDeclare.getInvestmentWay()));//投资方式
				        nameValuePairs.add(new BasicNameValuePair("statusFirst",StringUtils.isEmpty(projectDeclare.getManageStatus())?"":projectDeclare.getManageStatus()));//办理状态
				        nameValuePairs.add(new BasicNameValuePair("dollarMoney",StringUtils.isEmpty(projectDeclare.getTotalConvertDollar())?"":projectDeclare.getTotalConvertDollar())); //折合美元(万元)
				        nameValuePairs.add(new BasicNameValuePair("ExchangeMoney",StringUtils.isEmpty(projectDeclare.getTotalParitiesRMBorDollar())?"":projectDeclare.getTotalParitiesRMBorDollar()));//使用的汇率(人民币/美元)
				        nameValuePairs.add(new BasicNameValuePair("ProjectCap",StringUtils.isEmpty(projectDeclare.getProjectPrincipal())?"":projectDeclare.getProjectPrincipal()));//项目本金
				        nameValuePairs.add(new BasicNameValuePair("ProjectCapName",StringUtils.isEmpty(projectDeclare.getInvestorName())?"":projectDeclare.getInvestorName()));//项目资本金投资者名称
				        nameValuePairs.add(new BasicNameValuePair("ProjectCapNei",StringUtils.isEmpty(projectDeclare.getProjectCapital())?"":projectDeclare.getProjectCapital()));//项目资本金
				        nameValuePairs.add(new BasicNameValuePair("dollarProjectCap",StringUtils.isEmpty(projectDeclare.getCapitalConvertDollar())?"":projectDeclare.getCapitalConvertDollar())); //折合美元(万元)
				        nameValuePairs.add(new BasicNameValuePair("ExchangeProjectCap",StringUtils.isEmpty(projectDeclare.getCapitalParitiesRMBORDollar())?"":projectDeclare.getCapitalParitiesRMBORDollar()));//使用的汇率(人民币/美元)
				        
				        nameValuePairs.add(new BasicNameValuePair("regCountryArea",StringUtils.isEmpty(projectDeclare.getRegisterStateArea())?"":projectDeclare.getRegisterStateArea()));//注册国别地区
				        nameValuePairs.add(new BasicNameValuePair("contribution",StringUtils.isEmpty(projectDeclare.getContributiveLimit())?"":projectDeclare.getContributiveLimit()));//出资额(万元)
				        nameValuePairs.add(new BasicNameValuePair("fundedRatio",StringUtils.isEmpty(projectDeclare.getRatioOfInvestments())?"":projectDeclare.getRatioOfInvestments())); //出资比例%
				        nameValuePairs.add(new BasicNameValuePair("inveType",StringUtils.isEmpty(projectDeclare.getWaysOfInvestment())?"":projectDeclare.getWaysOfInvestment()));//出资方式
				        nameValuePairs.add(new BasicNameValuePair("isNewInve",StringUtils.isEmpty(projectDeclare.getNewFixedAssets())?"":projectDeclare.getNewFixedAssets()));//是否涉及新增固定资产投资
				        nameValuePairs.add(new BasicNameValuePair("landMethod",StringUtils.isEmpty(projectDeclare.getLandAcquisition())?"":projectDeclare.getLandAcquisition())); //土地获取方式
				        nameValuePairs.add(new BasicNameValuePair("landTotal",StringUtils.isEmpty(projectDeclare.getSumSiteArea())?"":projectDeclare.getSumSiteArea()));//总用地面积(平方米)
				        nameValuePairs.add(new BasicNameValuePair("buildTotal",StringUtils.isEmpty(projectDeclare.getSumCoveredArea())?"":projectDeclare.getSumCoveredArea()));//总建筑面积(平方米)
				        nameValuePairs.add(new BasicNameValuePair("isNewDevice",StringUtils.isEmpty(projectDeclare.getIsAddEquipment())?"":projectDeclare.getIsAddEquipment()));//是否新增设备
				        nameValuePairs.add(new BasicNameValuePair("impNubAndAmount",StringUtils.isEmpty(projectDeclare.getImportedEquipmentAndMoney())?"":projectDeclare.getImportedEquipmentAndMoney()));//其中：拟进口设备数量及金额
				        nameValuePairs.add(new BasicNameValuePair("isUnPrepar",StringUtils.isEmpty(projectDeclare.getPolicyTypes())?"":projectDeclare.getPolicyTypes()));//适用产业政策条目
				        
				        nameValuePairs.add(new BasicNameValuePair("policyEntry",StringUtils.isEmpty(projectDeclare.getProjectDeptIsBuild())?"":projectDeclare.getProjectDeptIsBuild()));//项目单位是否筹建中
				        
				        nameValuePairs.add(new BasicNameValuePair("otherDecl",""));//其他投资方式需给予申报的情况
				        nameValuePairs.add(new BasicNameValuePair("importEqmentMoeny",StringUtils.isEmpty(projectDeclare.getImportedEquipmentAndMoney())?"":projectDeclare.getImportedEquipmentAndMoney()));//项其中:拟进口设备数量及金额
				        nameValuePairs.add(new BasicNameValuePair("proCatalogType",""));//项目目录分类
				        //nameValuePairs.add(new BasicNameValuePair("catalogTypeNei",""));//目录分类
				        nameValuePairs.add(new BasicNameValuePair("recCatalogTypeNei",StringUtils.isEmpty(projectDeclare.getPutClassify())?"":projectDeclare.getPutClassify()));//备案目录分类
				        nameValuePairs.add(new BasicNameValuePair("catalogBeianTypeNei",StringUtils.isEmpty(projectDeclare.getPutCatalog())?"":projectDeclare.getPutCatalog()));//备案目录
				        nameValuePairs.add(new BasicNameValuePair("revCatalogTypeNei",StringUtils.isEmpty(projectDeclare.getCheckCatalogClassify())?"":projectDeclare.getCheckCatalogClassify()));//核准目录分类
				        nameValuePairs.add(new BasicNameValuePair("catalogHezhunTypeNei",StringUtils.isEmpty(projectDeclare.getCheckCatalog())?"":projectDeclare.getCheckCatalog()));//核准目录
				        nameValuePairs.add(new BasicNameValuePair("proCatalogTypeNei",""));//审核目录分类
				        nameValuePairs.add(new BasicNameValuePair("catalogShenpiTypeNei",""));//审批目录
				        nameValuePairs.add(new BasicNameValuePair("assDateNei",StringUtils.isEmpty(projectDeclare.getGiveDate())?"":projectDeclare.getGiveDate()));//赋码日期
				        nameValuePairs.add(new BasicNameValuePair("assOfficeNei",StringUtils.isEmpty(projectDeclare.getGiveDept())?"":projectDeclare.getGiveDept()));//赋码部门
				        nameValuePairs.add(new BasicNameValuePair("consNatureNei",StringUtils.isEmpty(projectDeclare.getBuildNature())?"":projectDeclare.getBuildNature()));//建设性质
				        nameValuePairs.add(new BasicNameValuePair("landAreaNei",StringUtils.isEmpty(projectDeclare.getSiteArea())?"":projectDeclare.getSiteArea()));//用地面积（公顷）
				        nameValuePairs.add(new BasicNameValuePair("addAreaNei",StringUtils.isEmpty(projectDeclare.getNewSiteArea())?"":projectDeclare.getNewSiteArea()));//新增用地面积（公顷
				        nameValuePairs.add(new BasicNameValuePair("AgriculturalAreaNei",StringUtils.isEmpty(projectDeclare.getFarmingSiteArea())?"":projectDeclare.getFarmingSiteArea()));//农用地面积（公顷
				        nameValuePairs.add(new BasicNameValuePair("funSocurceNei",StringUtils.isEmpty(projectDeclare.getCapitalSource())?"":projectDeclare.getCapitalSource()));//资金来源
				        nameValuePairs.add(new BasicNameValuePair("finanFunSocurceNei",StringUtils.isEmpty(projectDeclare.getFinancialCapitalSource())?"":projectDeclare.getFinancialCapitalSource()));//财政资金来源
				        nameValuePairs.add(new BasicNameValuePair("isTechRefProNei",StringUtils.isEmpty(projectDeclare.getIsTechnically())?"":projectDeclare.getIsTechnically()));//是否技改项目
				        nameValuePairs.add(new BasicNameValuePair("proNature",StringUtils.isEmpty(projectDeclare.getProjectDeptType())?"":projectDeclare.getProjectDeptType()));//项目单位性质
				        nameValuePairs.add(new BasicNameValuePair("proRegAddress",""));//项目单位注册地址
				        nameValuePairs.add(new BasicNameValuePair("businessScope",StringUtils.isEmpty(projectDeclare.getBusinessScope())?"":projectDeclare.getBusinessScope()));//主要经营范围
				        nameValuePairs.add(new BasicNameValuePair("email",StringUtils.isEmpty(projectDeclare.getEmail())?"":projectDeclare.getEmail()));//电子邮件
				        nameValuePairs.add(new BasicNameValuePair("fax","")); //传真
				        nameValuePairs.add(new BasicNameValuePair("policyEntryType",StringUtils.isEmpty(projectDeclare.getPolicyTypes())?"":projectDeclare.getPolicyTypes())); //适用产业政策条目类型
				        
						try {
							//企业、事业单位、社会团体等投资建设的固定资产投资项目核准 核准 非技改 789152c6f47f4f0989a5099d5d5d417a
							//企业投资技术改造项目核准 核准 技改  7173168e8e1d48f5a828275f752c5234 
							//审批 政府投资城建项目立项审批 01b8f8e45ac44e1e95a11ed8f6d83c38
							//企业技术改造项目备案 - 技改 备案 8609ed41c75042f4ad8d49d307fd2250
							
							/**
							 *  非技改/技改 备案项目 内资
							 */
							if(projectDeclare.getProjectType().indexOf(ProjectUtils.BEIAN_TYPE_NZ)!=-1&&projectDeclare.getIsForeign().equals("0")){
								 nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.BANZ_TRANID_FJG));
								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
								 JSONObject jsonObject=JSONObject.fromObject(res);
								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
									 projectDeclare.setBjbh("1");
									 int ret = projectDeclareService.updateBjbh(projectDeclare);
								 }
							}
//							/**
//							 * 非技改 备案项目 外资
//							 */
//							else if(projectDeclare.getProjectType().indexOf(ProjectUtils.BEIAN_TYPE_NZ)!=-1&&ProjectUtils.ISTECHNICALLY_NO.equals(projectDeclare.getIsTechnically())&&projectDeclare.getIsForeign().equals("1")){
//								nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.BANZ_TRANID_FJG));
//								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
//								 JSONObject jsonObject=JSONObject.fromObject(res);
//								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
//									 projectDeclare.setBjbh("1");
//									 int ret = projectDeclareService.updateBjbh(projectDeclare);
//								 }
//							}
							/**
							 * 技改/非技改 备案项目 内资
							 */
							else if(projectDeclare.getProjectType().indexOf(ProjectUtils.BEIAN_TYPE_NZ)!=-1&&projectDeclare.getIsForeign().equals("0")){
								nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.BANZ_TRANID_JG));
								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
								 JSONObject jsonObject=JSONObject.fromObject(res);
								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
									 projectDeclare.setBjbh("1");
									 int ret = projectDeclareService.updateBjbh(projectDeclare);
								 }
							}
							/**
							 *  技改 备案 外资
							 */
//							else if(projectDeclare.getProjectType().indexOf(ProjectUtils.BEIAN_TYPE_NZ)!=-1&&projectDeclare.getIsForeign().equals("1")){
//								nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.BANZ_TRANID_FJG));
//								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
//								 JSONObject jsonObject=JSONObject.fromObject(res);
//								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
//									 projectDeclare.setBjbh("1");
//									 int ret = projectDeclareService.updateBjbh(projectDeclare);
//								 }
//							}
							/**
							 * 核准 内资
							 */
							if(projectDeclare.getProjectType().indexOf(ProjectUtils.HEZHUN_TYPE_HZ)!=-1&&projectDeclare.getIsForeign().equals("0")){
								nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.HZ_TRANID_JG));
								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
								 JSONObject jsonObject=JSONObject.fromObject(res);
								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
									 projectDeclare.setBjbh("1");
									 int ret = projectDeclareService.updateBjbh(projectDeclare);
								 }
							}
							/**
							 * 核准 外资
							 */
							if(projectDeclare.getProjectType().indexOf(ProjectUtils.HEZHUN_TYPE_HZ)!=-1&&projectDeclare.getIsForeign().equals("1")){
								nameValuePairs.add(new BasicNameValuePair("trans.id",ProjectUtils.HZ_TRANID_FJG));
								 String res = HttpRequest.send(ProjectUtils.INTERFACEURL+ProjectUtils.SAVEBJ, nameValuePairs);
								 JSONObject jsonObject=JSONObject.fromObject(res);
								 if(null!=jsonObject && ProjectUtils.SP_SUCCESS.equals(String.valueOf(jsonObject.get("code")))){
									 projectDeclare.setBjbh("1");
									 int ret = projectDeclareService.updateBjbh(projectDeclare);
								 }
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(e.getMessage());
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
			e.printStackTrace();
		}
		return list;
	}
}
