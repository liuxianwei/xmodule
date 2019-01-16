package com.penglecode.xmodule.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;

/**
 * UPMS角色服务
 * 
 * @author 	pengpeng
 * @date	2019年1月15日 下午3:41:23
 */
public interface UpmsRoleService {

	/**
	 * 创建角色
	 * @param role
	 */
	public void createRole(UpmsRole role);
	
	/**
	 * 更新角色
	 * @param role
	 */
	public void updateRole(UpmsRole role);
	
	/**
	 * 根据角色id删除角色
	 * @param roleId
	 */
	public void deleteRoleById(Long roleId);
	
	/**
	 * 根据角色id获取角色
	 * @param roleId
	 */
	public UpmsRole getRoleById(Long roleId);
	
	/**
	 * 根据条件查询角色列表(排序、分页)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	public List<UpmsRole> getRoleListByPage(UpmsRole condition, Page page, Sort sort);
	
	/**
	 * 获取该角色的可见资源列表
	 * @param roleId
	 * @return
	 */
	public List<UpmsResource> getResourceListByRoleId(Long roleId);
	
	/**
	 * 配置该角色的可见资源
	 * @param roleId
	 * @param resourceIdList
	 * @param createBy		- 操作人ID
	 * @param createTime	- 操作时间
	 */
	public void configRoleResources(Long roleId, List<Long> resourceIdList, Long createBy, String createTime);
	
}
