package com.penglecode.xmodule.springcloud.busexample.model;

import com.penglecode.xmodule.common.support.BaseModel;

public class AppInfo implements BaseModel<AppInfo> {

	private static final long serialVersionUID = 1L;

	private Long appId;
	
	private String appName;
	
	private String appKey;
	
	private String appSecret;
	
	private String createTime;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
