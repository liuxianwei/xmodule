package com.penglecode.xmodule.common.support;

/**
 * 前后端分离开发模式的应用配置(包括应用的name, url, contextpath等)
 * 
 * @author 	pengpeng
 * @date	2018年9月30日 上午11:20:39
 */
public class MvvmWebAppConfig {
	
	/** 整个应用的web服务url(例如基于nginx)，例如: http://127.0.0.1 */
	private String globalWebServerUrl;
	
	/** 整个应用的静态文件服务url(例如基于nginx)，例如: http://127.0.0.1/static */
	private String globalFileServerUrl;
	
	/** 整个应用的静态文件真实存储根路径(例如基于nginx)，例如: d:/nginxfiles/static */
	private String globalFileServerRoot;
	
	/** 模块应用ID */
	private Long appId;
	
	/** 模块应用名称 */
	private String appName;
	
	/** 模块应用描述 */
	private String appDesc;
	
	/** 模块应用的web服务的url，例如: http://127.0.0.1/user，http://127.0.0.1/news */
	private String appWebServerUrl;
	
	/** 模块应用的web服务的contextPath，例如: /user，/news */
	private String appWebContextPath;
	
	/** 模块应用的api服务的url，例如: http://127.0.0.1/user-api，http://127.0.0.1/news-api */
	private String appApiServerUrl;
	
	/** 模块应用的api服务的contextPath，例如: /user-api，/news-api */
	private String appApiContextPath;

	public String getGlobalWebServerUrl() {
		return globalWebServerUrl;
	}

	public void setGlobalWebServerUrl(String globalWebServerUrl) {
		this.globalWebServerUrl = globalWebServerUrl;
	}

	public String getGlobalFileServerUrl() {
		return globalFileServerUrl;
	}

	public void setGlobalFileServerUrl(String globalFileServerUrl) {
		this.globalFileServerUrl = globalFileServerUrl;
	}

	public String getGlobalFileServerRoot() {
		return globalFileServerRoot;
	}

	public void setGlobalFileServerRoot(String globalFileServerRoot) {
		this.globalFileServerRoot = globalFileServerRoot;
	}

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

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public String getAppWebServerUrl() {
		return appWebServerUrl;
	}

	public void setAppWebServerUrl(String appWebServerUrl) {
		this.appWebServerUrl = appWebServerUrl;
	}

	public String getAppWebContextPath() {
		return appWebContextPath;
	}

	public void setAppWebContextPath(String appWebContextPath) {
		this.appWebContextPath = appWebContextPath;
	}

	public String getAppApiServerUrl() {
		return appApiServerUrl;
	}

	public void setAppApiServerUrl(String appApiServerUrl) {
		this.appApiServerUrl = appApiServerUrl;
	}

	public String getAppApiContextPath() {
		return appApiContextPath;
	}

	public void setAppApiContextPath(String appApiContextPath) {
		this.appApiContextPath = appApiContextPath;
	}

}
