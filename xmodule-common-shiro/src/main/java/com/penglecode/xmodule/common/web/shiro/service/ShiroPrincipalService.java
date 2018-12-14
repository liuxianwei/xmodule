package com.penglecode.xmodule.common.web.shiro.service;

import java.util.Map;
import java.util.Set;

import com.penglecode.xmodule.common.web.support.LoginToken;

/**
 * 针对Principal当事人的一些方法(例如获取用户[当事人]的角色集合、获取用户[当事人]的权限结合等..)
 * @param <U>	- 一般指用户账户
 * @param <RO>  - 一般指用户所拥有的角色
 * @param <RE>  - 一般指用户所拥有的资源
 * @author	  	pengpeng
 * @date	  	2015年1月30日 上午10:39:49
 * @version  	1.0
 */
public interface ShiroPrincipalService<U,RO,RE> {

	/**
	 * 根据principal获取当事人的角色代码集
	 * @param principal			- 当事人
	 * @param appId				- 所属应用ID
	 * @return
	 */
	public Set<String> getStringRoles(U principal, Long appId);
	
	/**
	 * 根据principal获取当事人的角色代码集
	 * @param principal
	 * @return	Map[key=appId, value=roles]
	 */
	public Map<Long,Set<String>> getStringRoles(U principal);
	
	/**
	 * 根据principal获取当事人的角色集合
	 * @param principal
	 * @param appId
	 * @return
	 */
	public Set<RO> getObjectRoles(U principal, Long appId);
	
	/**
	 * 根据principal获取当事人的角色代码集
	 * @param principal
	 * @return	Map[key=appId, value=roles]
	 */
	public Map<Long,Set<RO>> getObjectRoles(U principal);
	
	/**
	 * 根据principal获取当事人权限代码集
	 * @param principal			- 当事人
	 * @param appId				- 所属应用ID
	 * @return					- 返回用户所拥有的权限
	 */
	public Set<String> getStringPermissions(U principal, Long appId);
	
	/**
	 * 根据principal获取当事人权限代码集
	 * @param principal
	 * @return	Map[key=appId, value=permissions]
	 */
	public Map<Long,Set<String>> getStringPermissions(U principal);
	
	/**
	 * 根据principal获取当事人所拥有的资源列表
	 * @param principal			- 当事人
	 * @param appId				- 所属应用ID
	 * @return					- 返回用户所拥有的资源
	 */
	public Set<RE> getObjectResources(U principal, Long appId);
	
	/**
	 * 根据principal获取当事人所拥有的资源列表
	 * @param principal
	 * @return	Map[key=appId, value=resources]
	 */
	public Map<Long,Set<RE>> getObjectResources(U principal);
	
	/**
	 * 根据principal获取当事人的信息
	 * @param principal			- 当事人
	 * @return
	 */
	public U getPrincipalObject(String principal);
	
	/**
	 * 创建登录令牌
	 * @param loginUser
	 * @param appId
	 * @return
	 */
	public LoginToken<U> createLoginToken(U loginUser, Long appId);

}
