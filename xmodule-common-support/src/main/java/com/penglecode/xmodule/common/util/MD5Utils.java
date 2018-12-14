package com.penglecode.xmodule.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5编码工具类
 * 
 * @author 	pengpeng
 * @date   		2017年6月13日 下午5:25:48
 * @version 	1.0
 */
public class MD5Utils {

	public static String md5(String text) {
		return DigestUtils.md5Hex(text);
	}
	
	public static void main(String[] args) {
		System.out.println(md5("123456"));
	}
	
}
