package com.penglecode.xmodule.upms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.security.service.RoleResourceService;
import com.penglecode.xmodule.common.security.support.RoleResource;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Sort.Order;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceActionTypeEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceTypeEnum;
import com.penglecode.xmodule.upms.mapper.UpmsResourceMapper;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.service.UpmsResourceService;

@Service("upmsResourceService")
public class UpmsResourceServiceImpl implements UpmsResourceService, RoleResourceService {

	@Autowired
	private UpmsResourceMapper upmsResourceMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createResource(UpmsResource resource) {
		ValidationAssert.notNull(resource, "参数不能为空!");
		ValidationAssert.notEmpty(resource.getResourceName(), "资源名称不能为空!");
		ValidationAssert.notNull(resource.getParentResourceId(), "父级资源ID不能为空!");
		UpmsResource parentResource = upmsResourceMapper.selectModelById(resource.getParentResourceId());
		ValidationAssert.notNull(parentResource, "父级资源不存在!");
		ValidationAssert.isTrue(UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode().equals(parentResource.getActionType()), "只能在节点类型为'菜单'的资源下面新建子节点!");
		resource.setResourceId(null);
		resource.setAppRootResource(ObjectUtils.defaultIfNull(resource.getAppRootResource(), Boolean.FALSE));
		resource.setActionType(ObjectUtils.defaultIfNull(resource.getActionType(), UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode()));
		resource.setResourceType(UpmsResourceTypeEnum.RESOURCE_TYPE_NORMAL.getTypeCode());
		resource.setResourceUrl(StringUtils.defaultIfEmpty(resource.getResourceUrl(), null));
		resource.setPermissionExpression(StringUtils.defaultIfEmpty(resource.getPermissionExpression(), null));
		resource.setSiblingsIndex(ObjectUtils.defaultIfNull(resource.getSiblingsIndex(), 1));
		resource.setResourceIcon(StringUtils.defaultIfEmpty(resource.getResourceIcon(), GlobalConstants.DEFAULT_RESOURCE_ICON));
		resource.setIndexPage(ObjectUtils.defaultIfNull(resource.getIndexPage(), Boolean.FALSE));
		resource.setCreateBy(ObjectUtils.defaultIfNull(resource.getCreateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		resource.setCreateTime(DateTimeUtils.formatNow());
		try {
			if(resource.getIndexPage()) { //作为首页? (一个应用只有一个首页)
				upmsResourceMapper.resetResourceIndexPage();
			}
			upmsResourceMapper.insertModel(resource);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("RESOURCE_NAME"), "新增资源失败,该资源名称已经存在!");
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateResource(UpmsResource resource) {
		ValidationAssert.notNull(resource, "参数不能为空!");
		ValidationAssert.notNull(resource.getResourceId(), "资源ID不能为空!");
		ValidationAssert.notEmpty(resource.getResourceName(), "资源名称不能为空!");
		ValidationAssert.notNull(resource.getParentResourceId(), "父级资源ID不能为空!");
		UpmsResource parentResource = upmsResourceMapper.selectModelById(resource.getParentResourceId());
		ValidationAssert.notNull(parentResource, "父级资源不存在!");
		ValidationAssert.isTrue(UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode().equals(parentResource.getActionType()), "只能在节点类型为'菜单'的资源下面新建子节点!");
		
		UpmsResource presource = upmsResourceMapper.selectModelById(resource.getResourceId());
		ValidationAssert.notNull(presource, "该资源已经不存在了!");
		
		Integer childCount = upmsResourceMapper.selectChildResourceCountByParentId(resource.getResourceId()); //获取当前资源下的子资源个数
		if(childCount > 0) { //如果当前被修改资源存在子资源，那么它的actionType只能为'菜单'类型,不能修改
			resource.setActionType(UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode());
		}
		
		resource.setResourceId(null);
		resource.setActionType(ObjectUtils.defaultIfNull(resource.getActionType(), UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode()));
		resource.setResourceUrl(StringUtils.defaultIfEmpty(resource.getResourceUrl(), null));
		resource.setPermissionExpression(StringUtils.defaultIfEmpty(resource.getPermissionExpression(), null));
		resource.setSiblingsIndex(ObjectUtils.defaultIfNull(resource.getSiblingsIndex(), 1));
		resource.setResourceIcon(StringUtils.defaultIfEmpty(resource.getResourceIcon(), GlobalConstants.DEFAULT_RESOURCE_ICON));
		resource.setIndexPage(ObjectUtils.defaultIfNull(resource.getIndexPage(), Boolean.FALSE));
		resource.setUpdateBy(ObjectUtils.defaultIfNull(resource.getCreateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		resource.setUpdateTime(DateTimeUtils.formatNow());
		try {
			if(resource.getIndexPage()) { //作为首页? (一个应用只有一个首页)
				upmsResourceMapper.resetResourceIndexPage();
			}
			Map<String, Object> paramMap = resource.mapBuilder().withResourceName().withResourceUrl().withResourceIcon()
					.withActionType().withHttpMethod().withIndexPage().withParentResourceId().withPermissionExpression()
					.withSiblingsIndex().withUpdateBy().withUpdateTime().build();
			upmsResourceMapper.updateModelById(resource.getResourceId(), paramMap);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("RESOURCE_NAME"), "修改资源失败,该资源名称已经存在!");
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteResourceById(Long resourceId, boolean cascadeDelete) {
		ValidationAssert.notNull(resourceId, "资源ID不能为空!");
		UpmsResource delResource = upmsResourceMapper.selectModelById(resourceId);
		ValidationAssert.notNull(delResource, "该资源已经不存在了!");
		BusinessAssert.isTrue(!UpmsResourceTypeEnum.RESOURCE_TYPE_SYSTEM.getTypeCode().equals(delResource.getResourceType()), "删除资源失败,系统资源不允许删除!");
		Integer roleResources = upmsResourceMapper.selectRoleResourceCountByRoleId(resourceId);
		BusinessAssert.isTrue(roleResources == 0, String.format("删除资源失败,资源[%s]已经在使用不允许删除!", delResource.getResourceName()));
		if(cascadeDelete){
			List<UpmsResource> allMyAppResourceList = upmsResourceMapper.selectResourceListByParam(delResource.getAppId(), null, Boolean.TRUE);
			if(!CollectionUtils.isEmpty(allMyAppResourceList)){
				List<UpmsResource> childResourceList = new ArrayList<UpmsResource>();
				for(UpmsResource resource : allMyAppResourceList){
					if(resource.getResourceId().equals(resourceId)){
						childResourceList.add(resource);
					}
				}
				loadChildResources(allMyAppResourceList, resourceId, childResourceList);
				for(UpmsResource resource : childResourceList){
					BusinessAssert.isTrue(!UpmsResourceTypeEnum.RESOURCE_TYPE_SYSTEM.getTypeCode().equals(resource.getResourceType()), "删除资源失败,其子资源中存在系统资源不允许删除!");
					BusinessAssert.isTrue(!resource.isInuse(), String.format("删除资源失败,其子资源[%s]已经在使用不允许删除!", resource.getResourceName()));
				}
				for(UpmsResource resource : childResourceList){
					upmsResourceMapper.deleteModelById(resource.getResourceId());
				}
			}
		}else{
			upmsResourceMapper.deleteModelById(resourceId);
		}
	}
	
	private void loadChildResources(List<UpmsResource> allResourceList, Long parentResourceId, List<UpmsResource> childResourceList){
		if(!CollectionUtils.isEmpty(allResourceList)){
			for(UpmsResource resource : allResourceList){
				if(resource.getParentResourceId().equals(parentResourceId)){
					childResourceList.add(resource);
					loadChildResources(allResourceList, resource.getResourceId(), childResourceList);
				}
			}
		}
	}

	@Override
	public UpmsResource getResourceById(Long resourceId) {
		return ModelDecodeUtils.decodeModel(upmsResourceMapper.selectModelById(resourceId));
	}
	
	@Override
	public List<UpmsResource> getResourceListByParentId(Long parentResourceId, Integer actionType) {
		UpmsResource example = new UpmsResource();
		example.setParentResourceId(parentResourceId);
		example.setActionType(actionType);
		return ModelDecodeUtils.decodeModel(upmsResourceMapper.selectModelListByExample(example, Sort.by(Order.asc("siblingsIndex"))));
	}

	@Override
	public List<UpmsResource> getResourceListByPage(UpmsResource condition, Page page, Sort sort) {
		List<UpmsResource> dataList = ModelDecodeUtils.decodeModel(upmsResourceMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(upmsResourceMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<UpmsResource> getResourceListByParam(Long appId, Integer actionType, boolean fetchInuse) {
		return ModelDecodeUtils.decodeModel(upmsResourceMapper.selectResourceListByParam(appId, actionType, fetchInuse));
	}

	@Override
	public List<RoleResource> getAllRoleResourceMappings() {
		List<RoleResource> roleResources = upmsResourceMapper.selectAllRoleResourceMappings();
		if(!CollectionUtils.isEmpty(roleResources)) {
			return roleResources.stream().filter(roleResource -> {
				return !StringUtils.isEmpty(roleResource.getResourceUrl());
			}).collect(Collectors.toList());
		}
		return null;
	}

}
