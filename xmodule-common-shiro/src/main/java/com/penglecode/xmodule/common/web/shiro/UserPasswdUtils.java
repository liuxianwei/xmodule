package com.penglecode.xmodule.common.web.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 用户密码工具类
 * 
 * @author  pengpeng
 * @date 	 2015年4月5日 下午10:37:20
 * @version 1.0
 */
public class UserPasswdUtils {

	public static String encryptPassword(String password, String salt) {
		SimpleHash hash = new SimpleHash("md5", password, salt, 1);
		String encodedPassword = hash.toHex();
		return encodedPassword;
	}
	
	public static String encryptPassword(String password, String salt, int hashIterations) {
		SimpleHash hash = new SimpleHash("md5", password, salt, hashIterations);
		String encodedPassword = hash.toHex();
		return encodedPassword;
	}
	
	public static void main(String[] args) {
		String password = "123456";
		String salt = "xadmin" + "2018-03-01 13:52:48";
		System.out.println(encryptPassword(password, salt, 1)); //77b1860ed64fc58ba02900cfcc83fd3c
	}
	
}
