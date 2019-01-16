package com.penglecode.xmodule.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.model.UpmsUser;

/**
 * UPMS用户服务
 * 
 * @author 	pengpeng
 * @date	2019年1月15日 下午4:13:23
 */
public interface UpmsUserService {

	/**
	 * 创建用户
	 * @param user
	 */
	public void createUser(UpmsUser user);
	
	/**
	 * 更新用户
	 * @param user
	 */
	public void updateUser(UpmsUser user);
	
	/**
	 * 根据用户id删除用户
	 * @param userId
	 */
	public void deleteUserById(Long userId);
	
	/**
	 * 更新用户状态
	 * @param userId
	 * @param status
	 */
	public void updateUserStatus(Long userId, Integer status);
	
	/**
     * 更新用户登录时间
     * 
     * @param userId
     * @param lastLoginTime
     */
    public void updateLoginTime(Long userId, String lastLoginTime);
    
    /**
     * 用户修改密码
     * 
     * @param user			- user[userId, password, repassword]必填
     * @param forceUpdate 	- 是否强制更新密码,若为false则user[oldpassword]必填
     */
    public void updatePassword(UpmsUser user, boolean forceUpdate);
	
	/**
	 * 根据用户id获取用户
	 * @param userId
	 * @return
	 */
	public UpmsUser getUserById(Long userId);
	
	/**
	 * 根据用户名获取用户
	 * @param userName
	 * @return
	 */
	public UpmsUser getUserByUserName(String userName);
	
	/**
	 * 根据条件查询用户列表(排序、分页)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	public List<UpmsUser> getUserListByPage(UpmsUser condition, Page page, Sort sort);
	
	/**
	 * 根据用户ID获取用户的角色列表
	 * @param userId		- 所属用户ID(必填)
	 * @param appId			- 所属应用ID(可选)
	 * @param condition		- 过滤条件(可选)
	 * @return
	 */
	public List<UpmsRole> getUserRoleList(Long userId, Long appId, UpmsRole condition);
	
	/**
	 * 根据用户ID获取用户的资源列表
	 * @param userId		- 所属用户ID(必填)
	 * @param appId			- 所属应用ID(可选)
	 * @param actionType	- 资源动作类型(可选)
	 * @return
	 */
	public List<UpmsResource> getUserResourceList(Long userId, Long appId, Integer actionType);
	
	/**
     * 添加用户-角色配置
	 * @param userId		- 所属用户ID(必填)
     * @param roleIdList
     * @param createBy 		- 操作人ID
     * @param createTime 	- 操作时间
     */
    public void addUserRoles(UpmsUser user, List<Long> roleIdList, Long createBy, String createTime);
    
    /**
     * 删除用户-角色配置
	 * @param userId		- 所属用户ID(必填)
     * @param roleIdList
     */
    public void delUserRoles(UpmsUser user, List<Long> roleIdList);
    
    /**
	 * 获取用户所能看见的应用的首页列表
	 * @param userId		- 必填
	 * @return
	 */
	public List<UpmsResource> getUserIndexResourceList(Long userId);
    
}
