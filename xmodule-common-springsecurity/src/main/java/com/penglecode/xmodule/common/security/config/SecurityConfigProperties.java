package com.penglecode.xmodule.common.security.config;

/**
 * 前后端分离开发时，定义给前端的页面配置
 * 
 * @author 	pengpeng
 * @date	2018年10月26日 下午12:52:00
 */
public class SecurityConfigProperties {

	/**
	 * 登录页面
	 */
	private String loginUrl;
	
	/**
	 * 登录成功的默认重定向页面
	 */
	private String defaultLoginSuccessUrl;
	
	/**
	 * 未授权页面(访问非Ajax请求时)
	 */
	private String unauthorizedUrl;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getDefaultLoginSuccessUrl() {
		return defaultLoginSuccessUrl;
	}

	public void setDefaultLoginSuccessUrl(String defaultLoginSuccessUrl) {
		this.defaultLoginSuccessUrl = defaultLoginSuccessUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	
}
