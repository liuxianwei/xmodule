package com.penglecode.xmodule.springsecurity.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;

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
	
}
