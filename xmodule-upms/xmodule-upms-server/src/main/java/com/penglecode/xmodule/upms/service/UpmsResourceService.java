package com.penglecode.xmodule.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.upms.model.UpmsResource;

/**
 * UPMS资源服务
 * 
 * @author 	pengpeng
 * @date	2019年1月15日 下午3:16:29
 */
public interface UpmsResourceService {

	/**
	 * 创建资源
	 * @param resource
	 */
	public void createResource(UpmsResource resource);
	
	/**
	 * 更新资源
	 * @param resource
	 */
	public void updateResource(UpmsResource resource);
	
	/**
	 * 根据资源id删除资源
	 * @param resourceId
	 * @param cascadeDelete
	 */
	public void deleteResourceById(Long resourceId, boolean cascadeDelete);
	
	/**
	 * 根据资源id获取资源
	 * @param resourceId
	 * @return
	 */
	public UpmsResource getResourceById(Long resourceId);
	
	/**
	 * 根据父资源id获取直接子资源列表
	 * @param parentResourceId
	 * @param actionType		- 为空则查询全部
	 * @return
	 */
	public List<UpmsResource> getResourceListByParentId(Long parentResourceId, Integer actionType);
	
	/**
	 * 根据条件查询资源列表(排序、分页)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	public List<UpmsResource> getResourceListByPage(UpmsResource condition, Page page, Sort sort);
	
	/**
	 * 根据应用ID获取资源列表
	 * @param appId			- 为空则查询所有
	 * @param actionType 	- 功能类型,为空则查询所有
	 * @param fetchInuse	- 是否带出使用状态
	 * @return
	 */
	public List<UpmsResource> getResourceListByParam(Long appId, Integer actionType, boolean fetchInuse);
	
}
