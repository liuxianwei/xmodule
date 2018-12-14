package com.penglecode.xmodule.common.web.shiro.filter;

public interface UnauthenticatedUrlAware {

	public String getUnauthenticatedUrl();

	public void setUnauthenticatedUrl(String unauthenticatedUrl);
	
}
