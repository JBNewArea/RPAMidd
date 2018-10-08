package com.rpa.task;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.rpa.utils.CompanyUtils;

/**
 * 校验 服务器 是否正常
 * @author Arthur
 *
 */
public class Proof {

	public void execute(){
		System.out.println(345);
	}
	
	public static void main(String[] args) {
		System.out.println(isReachable("10.196.109.218"));
	}
	
	 
	/**
	 * 校验IP是否连
	 * @param remoteInetAddr
	 * @return
	 */
	public static boolean isReachable(String remoteInetAddr){
		boolean reachable = false;
		try {
			InetAddress address = InetAddress.getByName(remoteInetAddr);
			reachable = address.isReachable(1500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reachable;
	}

}
