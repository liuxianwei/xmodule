package com.penglecode.xmodule.common.security.service;

import java.util.List;

import com.penglecode.xmodule.common.security.support.RoleResource;

/**
 * 获取角色-资源关系列表
 * 
 * @author 	pengpeng
 * @date	2018年10月30日 下午4:12:18
 */
public interface RoleResourceService {

	public List<RoleResource> getAllRoleResourceMappings();
	
}
