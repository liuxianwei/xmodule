package com.penglecode.xmodule.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;

@DefaultDatabase
public interface UpmsRoleMapper extends BaseMybatisMapper<UpmsRole> {
	
	/**
	 * 查询指定角色下关联的用户-角色关系个数
	 * @param roleId
	 * @return
	 */
	public Integer selectUserRoleCountByRoleId(Long roleId);
	
	/**
	 * 查询指定角色下关联的资源列表
	 * @param roleId
	 * @return
	 */
	public List<UpmsResource> selectResourceListByRoleId(Long roleId);
	
	/**
	 * 批量插入角色-资源关系
	 * @param roleId
	 * @param resourceIdList
	 * @param createBy
	 * @param createTime
	 */
	public void insertRoleResources(@Param("roleId")Long roleId, @Param("resourceIdList")List<Long> resourceIdList, @Param("createBy")Long createBy, @Param("createTime")String createTime);
	
	/**
	 * 删除某角色下的所有角色-资源配置关系
	 * @param roleId
	 * @return
	 */
	public Integer deleteRoleResourcesByRoleId(Long roleId);
	
	/**
	 * 统计指定appId的角色个数
	 * @param appId
	 * @return
	 */
	public Integer selectAllRoleCountByAppId(Long appId);
	
}