package com.penglecode.xmodule.common.web.shiro.service;

import java.util.List;

import com.penglecode.xmodule.common.web.shiro.support.ShiroPermissionConfig;
/**
 * 系统中定义的全部url=perm配置服务
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月28日 下午5:20:41
 * @version  	1.0
 */
public interface ShiroPermissionService {

	/**
	 * 获取某个应用中定义的url=perm配置
	 * @param appId		- 如果appId为null则加载全部url=perm配置
	 * @return
	 */
	public List<ShiroPermissionConfig> getAppPermissionConfig(Long appId);
	
}
