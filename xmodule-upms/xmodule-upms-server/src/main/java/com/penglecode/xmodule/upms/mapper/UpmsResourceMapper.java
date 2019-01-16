package com.penglecode.xmodule.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.common.web.security.support.RoleResource;
import com.penglecode.xmodule.upms.model.UpmsResource;

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
	 * @param appId			- 所属应用ID(为空则查询所有)
	 * @param actionType	- 资源功能类型(为空则查询所有)
	 * @param fetchInuse	- 是否带出inuse状态
	 * @return
	 */
	public List<UpmsResource> selectResourceListByParam(@Param("appId") Long appId, @Param("actionType") Integer actionType, @Param("fetchInuse") boolean fetchInuse);
	
	/**
	 * 获取所有资源-角色关系列表
	 * @return
	 */
	public List<RoleResource> selectAllRoleResourceMappings();
	
	/**
	 * 统计指定appId下的资源个数
	 * @param appId
	 * @return
	 */
	public Integer selectAllResourceCountByAppId(Long appId);
	
	/**
	 * 删除指定appId下的所有资源
	 * @param appId
	 * @return
	 */
	public Integer deleteResourcesByAppId(Long appId);
	
}