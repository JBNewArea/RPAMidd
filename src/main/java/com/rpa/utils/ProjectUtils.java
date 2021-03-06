package com.rpa.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 江苏省投资项目在线监管平台 静态资源
 * @author Arthur
 *
 */
public class ProjectUtils {
	
	/**
	 * 登录地址
	 */
	public final static String SYSLOGIN_URL="http://10.101.79.24:11002/zwService/";
	/**
	 * 请求列表地址
	 */
	public final static String LIST_URL="http://10.101.79.24:11002/zwService/";
	
	public final static String BYONE_URL="http://10.101.79.24:11002/zwService/";
	/**
	 * 单个事项上传材料
	 */
	public final static String ONLIEN_MATERITALSAVE_URL="userMaterial/userMaterial/save";
	
	/**
	 * 接口项目地址
	 */
	public final static String INTERFACEURL="http://221.226.86.27:8090/xzsp-interface/a/rpc/";
	//public final static String INTERFACEURL="http://59.83.223.11:8099/xzsp-interface/a/rpc/";
	public final static String MATTERINFO_URL="http://221.226.86.27:8090/xzsp-interface/a/rpc/trans/trans/showList";
	
	public final static String SAVEBJ="application/application/save";
	public final static String MATTERINFO_ACTIONNAMEURL="trans/trans/findOne";
	public final static String MATTERFILEURL="material/material/findMaterialList";
	
	public final static String PRELIMINARY_URL="http://10.101.79.24:11002/zwService/";
	/**
	 * 通用参数 - 服务器
	 */
	public final static String  IW_APIKEY="123";
	public final static String  USERNAME="njjbxqcx";
	public final static String  PWD="888888";
	
	/**
	 * 通用参数 - 审批账号
	 */
	public final static String USERNAME_SP="njjbxqxz";
	public final static String PWD_SP="Njjbxq88";
	
	
	public final static String  IW_CMD_LOGIN="sysLogin";
	public final static String  IW_CMD_LIST = "proList";
	public final static String  IW_CMD_DETAIL="proDetail";
	public final static String  IW_CMD_PRETRIAL="pretrialList";
	public final static String  IW_CMD_PREDETAIL="pretrialDetail";
	
	
	public final static String  SUCCESS_CODE="000000";
	public final static String  ERROR_CODE="01111";
	
	/**
	 * 事项 - 企业投资建设固定资产投资项目备案
	 */
	public final static String ISTECHNICALLY_NO="否";
	public final static String ISTECHNICALLY_YES="是";
	public final static String SP_SUCCESS="8888";
	public final static String BEIAN_TYPE_NZ="备案";
	public final static String HEZHUN_TYPE_HZ="核准";
	public final static String NZ="内资";
	public final static String WZ="外";
	public final static String BANZ_TRANID_FJG="e8f74efb01ce443faf91f77c064ad781";
	public final static String BANZ_TRANID_JG="8609ed41c75042f4ad8d49d307fd2250";
	
	public final static String HZ_TRANID_JG="7173168e8e1d48f5a828275f752c5234";
	public final static String HZ_TRANID_FJG="789152c6f47f4f0989a5099d5d5d417a";
	
	public final static String PROJECT_ANTISTOP="统一社会信用";
	public final static String PROJECT_ANTISTOP_1="其他";
	
	
	/**
	 * 企业投资建设固定资产投资项目备案   非技改 备案
	 */
	public final static String COMPANY_QY = "e8f74efb01ce443faf91f77c064ad781";
	
	
	public static void downloadNet(String path) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(path);
        try {
        	String fileType ="";
        	String[] strs=path.split("\\.");
        	for(int i=0,len=strs.length;i<len;i++){
        	  System.out.println(strs[i].toString());
        	  fileType =strs[i].toString();
        	}
        	String downliadPath = "C:/downloadNet/file."+fileType;
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(downliadPath);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static void main(String[] args) {
		try {
			downloadNet("http://10.101.79.24/files/e58c963196cb4a66b22768584c7ffe27.pdf");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
