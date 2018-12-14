package com.penglecode.xmodule.springsecurity.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsResource;

public interface UpmsResourceService {

	public void createResource(UpmsResource resource);
	
	public void updateResource(UpmsResource resource);
	
	public void deleteResourceById(Long resourceId, boolean cascadeDelete);
	
	public UpmsResource getResourceById(Long resourceId);
	
	public List<UpmsResource> getResourceListByParentId(Long parentResourceId, Integer actionType);
	
	public List<UpmsResource> getResourceListByPage(UpmsResource condition, Page page, Sort sort);
	
	public List<UpmsResource> getResourceListByParam(Integer actionType, boolean fetchInuse);
	
}
