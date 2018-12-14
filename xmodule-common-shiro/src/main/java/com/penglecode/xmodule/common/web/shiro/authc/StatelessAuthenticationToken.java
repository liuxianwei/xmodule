package com.penglecode.xmodule.common.web.shiro.authc;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 无状态认证token
 * 
 * @author 	pengpeng
 * @date	2018年1月4日 上午11:04:09
 */
public class StatelessAuthenticationToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;

	/** 应用Key */
	private String appKey;
	
	/** uuid,例如：8468cc07b7d94051b47d832b2114d5e1 */
	private String token;
	
	/** 客户端请求时间戳 */
	private long timestamp;
	
	/** 参与签名的额外数据 */
	private Map<String,String> signParams;
	
	/** 客户端数据签名 */
	private String signstr;
	
	/** 服务端响应时间 */
	private long respondTime;
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, String> getSignParams() {
		return signParams;
	}

	public void setSignParams(Map<String, String> signParams) {
		this.signParams = signParams;
	}

	public String getSignstr() {
		return signstr;
	}

	public void setSignstr(String signstr) {
		this.signstr = signstr;
	}

	public long getRespondTime() {
		return respondTime;
	}

	public void setRespondTime(long respondTime) {
		this.respondTime = respondTime;
	}

	public Object getPrincipal() {
		return getAppKey();
	}

	public Object getCredentials() {
		return getSignstr();
	}

}
