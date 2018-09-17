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
	 * 接口项目地址
	 */
	public final static String INTERFACEURL="http://221.226.86.27:8090/xzsp-interface/a/rpc/";
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
	public final static String ISTECHNICALLY="否";
	public final static String SP_SUCCESS="8888";
	
	public final static String PROJECT_ANTISTOP="统一社会信用";
	public final static String PROJECT_ANTISTOP_1="其他";
	
	
	public static void downloadNet(String path) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(path);
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("/Users/Arthur/Desktop/qqq.jpg");

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
}
