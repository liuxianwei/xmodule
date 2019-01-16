package com.penglecode.xmodule.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.model.UpmsUser;

@DefaultDatabase
public interface UpmsUserMapper extends BaseMybatisMapper<UpmsUser> {
	
	/**
     * 获取用户所拥有的角色列表
     * @param userId		- 必填
     * @param appId			- 可选
     * @param condition		- 可选
     * @return
     */
    public List<UpmsRole> selectUserRoleList(@Param("appId")Long appId, @Param("userId")Long userId, @Param("condition")UpmsRole condition);
	
	/**
     * 添加用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     * @param createBy 		- 操作人id
     * @param createTime 	- 操作时间
     */
    public void insertUserRoles(@Param("userId")Long userId, @Param("roleIdList")List<Long> roleIdList, @Param("createBy")Long createBy, @Param("createTime")String createTime);
    
    /**
     * 删除用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     */
    public void deleteUserRoles(@Param("userId")Long userId, @Param("roleIdList")List<Long> roleIdList);
    
    /**
     * 删除用户的所有角色
     * 
     * @param userId
     * @param roleIdList
     */
    public void deleteUserAllRoles(Long userId);
    
    /**
	 * 获取用户所能看见的应用的首页列表
	 * @param userId		- 必填
	 * @return
	 */
	public List<UpmsResource> selectUserIndexResourceList(Long userId);
	
	/**
	 * 获取用户资源列表
	 * @param userId		- 必填
	 * @param appId			- 可选
	 * @param actionType	- 可选
	 * @return
	 */
	public List<UpmsResource> selectUserResourceList(@Param("userId")Long userId, @Param("appId")Long appId, @Param("actionType")Integer actionType);
    
}