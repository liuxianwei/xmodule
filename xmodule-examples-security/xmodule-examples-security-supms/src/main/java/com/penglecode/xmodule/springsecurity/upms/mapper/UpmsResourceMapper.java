package com.penglecode.xmodule.springsecurity.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.security.support.RoleResource;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsResource;

@DefaultDatabase
public interface UpmsResourceMapper extends BaseMybatisMapper<UpmsResource> {
	
	/**
	 * 重置所有indexPage为false
	 */
	public void resetResourceIndexPage();
	
	/**
	 * 查询指定父资源下的直接子资源的个数
	 * @param parentResourceId
	 * @return
	 */
	public Integer selectChildResourceCountByParentId(Long parentResourceId);
	
	/**
	 * 获取指定资源的角色-资源关系个数
	 * @param resourceId
	 * @return
	 */
	public Integer selectRoleResourceCountByRoleId(Long resourceId);
	
	/**
	 * 获取所有资源列表
	 * @param actionType	- 资源功能类型(可为空)
	 * @param fetchInuse	- 是否带出inuse状态
	 * @return
	 */
	public List<UpmsResource> selectResourceListByParam(@Param("actionType") Integer actionType, @Param("fetchInuse") boolean fetchInuse);
	
	/**
	 * 获取所有资源-角色关系列表
	 * @return
	 */
	public List<RoleResource> selectAllRoleResourceMappings();
	
}