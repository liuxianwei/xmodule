package com.penglecode.xmodule.springsecurity.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsRole;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;

@DefaultDatabase
public interface UpmsUserMapper extends BaseMybatisMapper<UpmsUser> {
	
	/**
	 * 根据用户ID查询用户-角色关系列表
	 * @param userId
	 * @return
	 */
	public List<UpmsRole> selectUserRoleListByUserId(Long userId);
	
	/**
     * 添加用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     * @param optUserId - 操作人id
     * @param optTime - 操作时间
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
	
}