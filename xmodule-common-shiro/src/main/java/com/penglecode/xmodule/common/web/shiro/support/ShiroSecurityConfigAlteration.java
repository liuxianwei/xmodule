package com.penglecode.xmodule.common.web.shiro.support;

import java.io.Serializable;

/**
 * shiro权限配置信息(用户-角色-资源)的变动
 * 
 * @author 	pengpeng
 * @date	2018年6月4日 上午9:39:07
 */
public class ShiroSecurityConfigAlteration implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 受变动的应用 */
	private String alterApp;
	
	/** 变动的用户(*代表全部) */
	private String alterUsers;
	
	/** 用户变动的类型 */
	private AlterationType alterUserType;
	
	/** 变动的角色(*代表全部) */
	private String alterRoles;
	
	/** 角色变动的类型 */
	private AlterationType alterRoleType;
	
	/** 变动的资源(*代表全部) */
	private String alterResources;
	
	/** 资源变动的类型 */
	private AlterationType alterResourceType;
	
	public String getAlterApp() {
		return alterApp;
	}

	public void setAlterApp(String alterApp) {
		this.alterApp = alterApp;
	}

	public String getAlterUsers() {
		return alterUsers;
	}

	public void setAlterUsers(String alterUsers) {
		this.alterUsers = alterUsers;
	}

	public AlterationType getAlterUserType() {
		return alterUserType;
	}

	public void setAlterUserType(AlterationType alterUserType) {
		this.alterUserType = alterUserType;
	}

	public String getAlterRoles() {
		return alterRoles;
	}

	public void setAlterRoles(String alterRoles) {
		this.alterRoles = alterRoles;
	}

	public AlterationType getAlterRoleType() {
		return alterRoleType;
	}

	public void setAlterRoleType(AlterationType alterRoleType) {
		this.alterRoleType = alterRoleType;
	}

	public String getAlterResources() {
		return alterResources;
	}

	public void setAlterResources(String alterResources) {
		this.alterResources = alterResources;
	}

	public AlterationType getAlterResourceType() {
		return alterResourceType;
	}

	public void setAlterResourceType(AlterationType alterResourceType) {
		this.alterResourceType = alterResourceType;
	}

	public static enum AlterationType {
		
		CREATED, UPDATED, DELETED
		
	}
	
}
