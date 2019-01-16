package com.penglecode.xmodule.common.web.security.support;

import com.penglecode.xmodule.common.support.BaseModel;

public class RoleResource implements BaseModel<RoleResource> {

	private static final long serialVersionUID = 1L;

	private String roleCode;
	
	private String resourceUrl;
	
	private String httpMethod;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
}
