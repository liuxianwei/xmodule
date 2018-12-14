package com.penglecode.xmodule.common.web.shiro.support;

import java.io.Serializable;

public class ShiroPermissionConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long appId;
	
	private String appWebContextPath;
	
	private String urlPattern;
	
	private String permission;

	public ShiroPermissionConfig() {
		super();
	}

	public ShiroPermissionConfig(Long appId, String appWebContextPath, String urlPattern, String permission) {
		super();
		this.appId = appId;
		this.appWebContextPath = appWebContextPath;
		this.urlPattern = urlPattern;
		this.permission = permission;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppWebContextPath() {
		return appWebContextPath;
	}

	public void setAppWebContextPath(String appWebContextPath) {
		this.appWebContextPath = appWebContextPath;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
