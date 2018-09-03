/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.rpa.pojo;
import java.util.Date;

/**
 * 项目入库Entity
 * @author freedom_taojie
 * @version 2018-04-10
 */
public class ProjectDeclare {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String declareDate;		// 申报时间
	private String projectCode;		// 项目代码
	private String projectType;		// 项目类型
	private String projectAttribute;		// 项目属性
	private String projectName;		// 项目名称
	private String projectUnit;		// 申报单位
	private String legalUnitInfo;		// 法人单位信息
	private String money;		// 项目投资额
	private String guobiao;		// 国标行业
	private String guanli;		// 管理行业
	private String buildPlace;		// 建设地点
	private String buildDetailPlace;		// 建设详细地址
	private String scaleandinfo;		// 建设规模及内容
	private String name;		// 姓名
	private String phone;		// 联系方式
	
	private String nameFirst;	//名称
	private String statusFirst;	//办理状态
	private String wenhaoFirst;	//办结文号
	private Date riqiFirst;	//日期
	private String bumenFirst;	//部门
	private Date caozuodateFirst;	//实际操作时间
	
	
	
	private String mainProjectName;//主项目名称
	private String giveDate;//赋码日期
	private String giveDept;//赋码部门
	private String startPlanDate;//拟开工时间
	private String endPlanDate;//拟结束时间
	private String buildNature;//建设性质
	private String totalInvestment;//总投资
	private String siteArea;//用地面积
	private String newSiteArea;//新增用地面积
	private String farmingSiteArea;//农用地面积
	private String projectPrincipal;//项目本金
	private String capitalSource;//资金来源
	private String financialCapitalSource;//财政资金来源
	private String isTechnically;//是否技改
	private String putClassify;//备案目录分类
	private String putCatalog;//备案目录
	private String projectLegalDetp;//项目法人单位
	private String projectLegalLicenseType;//项目法人证照类型
	private String projectLegalLicenseNum;//项目法人证照号码
	private String linkman;//联系人
	private String linktel;//联系电话
	private String projectState;//项目状态
	private String manageStatus;//办理状态
	
	
	

	public String getDeclareDate() {
		return declareDate;
	}

	public void setDeclareDate(String declareDate) {
		this.declareDate = declareDate;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectAttribute() {
		return projectAttribute;
	}

	public void setProjectAttribute(String projectAttribute) {
		this.projectAttribute = projectAttribute;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}

	public String getLegalUnitInfo() {
		return legalUnitInfo;
	}

	public void setLegalUnitInfo(String legalUnitInfo) {
		this.legalUnitInfo = legalUnitInfo;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getGuobiao() {
		return guobiao;
	}

	public void setGuobiao(String guobiao) {
		this.guobiao = guobiao;
	}

	public String getGuanli() {
		return guanli;
	}

	public void setGuanli(String guanli) {
		this.guanli = guanli;
	}

	public String getBuildPlace() {
		return buildPlace;
	}

	public void setBuildPlace(String buildPlace) {
		this.buildPlace = buildPlace;
	}

	public String getBuildDetailPlace() {
		return buildDetailPlace;
	}

	public void setBuildDetailPlace(String buildDetailPlace) {
		this.buildDetailPlace = buildDetailPlace;
	}

	public String getScaleandinfo() {
		return scaleandinfo;
	}

	public void setScaleandinfo(String scaleandinfo) {
		this.scaleandinfo = scaleandinfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

	public String getStatusFirst() {
		return statusFirst;
	}

	public void setStatusFirst(String statusFirst) {
		this.statusFirst = statusFirst;
	}

	public String getWenhaoFirst() {
		return wenhaoFirst;
	}

	public void setWenhaoFirst(String wenhaoFirst) {
		this.wenhaoFirst = wenhaoFirst;
	}

	public Date getRiqiFirst() {
		return riqiFirst;
	}

	public void setRiqiFirst(Date riqiFirst) {
		this.riqiFirst = riqiFirst;
	}

	public String getBumenFirst() {
		return bumenFirst;
	}

	public void setBumenFirst(String bumenFirst) {
		this.bumenFirst = bumenFirst;
	}

	public Date getCaozuodateFirst() {
		return caozuodateFirst;
	}

	public void setCaozuodateFirst(Date caozuodateFirst) {
		this.caozuodateFirst = caozuodateFirst;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMainProjectName() {
		return mainProjectName;
	}

	public void setMainProjectName(String mainProjectName) {
		this.mainProjectName = mainProjectName;
	}

	public String getGiveDate() {
		return giveDate;
	}

	public void setGiveDate(String giveDate) {
		this.giveDate = giveDate;
	}

	public String getGiveDept() {
		return giveDept;
	}

	public void setGiveDept(String giveDept) {
		this.giveDept = giveDept;
	}

	public String getStartPlanDate() {
		return startPlanDate;
	}

	public void setStartPlanDate(String startPlanDate) {
		this.startPlanDate = startPlanDate;
	}

	public String getEndPlanDate() {
		return endPlanDate;
	}

	public void setEndPlanDate(String endPlanDate) {
		this.endPlanDate = endPlanDate;
	}

	public String getBuildNature() {
		return buildNature;
	}

	public void setBuildNature(String buildNature) {
		this.buildNature = buildNature;
	}

	public String getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(String totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public String getSiteArea() {
		return siteArea;
	}

	public void setSiteArea(String siteArea) {
		this.siteArea = siteArea;
	}

	public String getNewSiteArea() {
		return newSiteArea;
	}

	public void setNewSiteArea(String newSiteArea) {
		this.newSiteArea = newSiteArea;
	}

	public String getFarmingSiteArea() {
		return farmingSiteArea;
	}

	public void setFarmingSiteArea(String farmingSiteArea) {
		this.farmingSiteArea = farmingSiteArea;
	}

	public String getProjectPrincipal() {
		return projectPrincipal;
	}

	public void setProjectPrincipal(String projectPrincipal) {
		this.projectPrincipal = projectPrincipal;
	}

	public String getCapitalSource() {
		return capitalSource;
	}

	public void setCapitalSource(String capitalSource) {
		this.capitalSource = capitalSource;
	}

	public String getFinancialCapitalSource() {
		return financialCapitalSource;
	}

	public void setFinancialCapitalSource(String financialCapitalSource) {
		this.financialCapitalSource = financialCapitalSource;
	}

	public String getIsTechnically() {
		return isTechnically;
	}

	public void setIsTechnically(String isTechnically) {
		this.isTechnically = isTechnically;
	}

	public String getPutClassify() {
		return putClassify;
	}

	public void setPutClassify(String putClassify) {
		this.putClassify = putClassify;
	}

	public String getPutCatalog() {
		return putCatalog;
	}

	public void setPutCatalog(String putCatalog) {
		this.putCatalog = putCatalog;
	}

	public String getProjectLegalDetp() {
		return projectLegalDetp;
	}

	public void setProjectLegalDetp(String projectLegalDetp) {
		this.projectLegalDetp = projectLegalDetp;
	}

	public String getProjectLegalLicenseType() {
		return projectLegalLicenseType;
	}

	public void setProjectLegalLicenseType(String projectLegalLicenseType) {
		this.projectLegalLicenseType = projectLegalLicenseType;
	}

	public String getProjectLegalLicenseNum() {
		return projectLegalLicenseNum;
	}

	public void setProjectLegalLicenseNum(String projectLegalLicenseNum) {
		this.projectLegalLicenseNum = projectLegalLicenseNum;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinktel() {
		return linktel;
	}

	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}

	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	public String getManageStatus() {
		return manageStatus;
	}

	public void setManageStatus(String manageStatus) {
		this.manageStatus = manageStatus;
	}
}