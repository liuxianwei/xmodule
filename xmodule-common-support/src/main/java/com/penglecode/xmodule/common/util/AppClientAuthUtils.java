package com.penglecode.xmodule.common.util;

import org.springframework.util.Assert;

/**
 * 应用客户端认证工具类
 * 
 * @author 	pengpeng
 * @date	2018年1月3日 下午5:15:48
 */
public class AppClientAuthUtils {

	/**
	 * 
	 * @param appKey
	 * @param appSecret
	 * @param clientType
	 * @param timestamp
	 * @return
	 */
	public static String createSign(String appKey, String appSecret, String clientType, long timestamp) {
		Assert.hasText(appKey, "参数appKey不能为空!");
		Assert.hasText(appSecret, "参数appSecret不能为空!");
		Assert.hasText(clientType, "参数clientType不能为空!");
		Assert.notNull(timestamp, "参数timestamp不能为空!");
		String datagram = appKey + "." + clientType + "." + timestamp + "." + appSecret;
		return MD5Utils.md5(datagram);
	}
	
}
