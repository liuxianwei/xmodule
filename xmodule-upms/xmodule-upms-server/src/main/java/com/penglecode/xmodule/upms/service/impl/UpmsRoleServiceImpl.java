package com.penglecode.xmodule.upms.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.upms.consts.em.UpmsRoleTypeEnum;
import com.penglecode.xmodule.upms.mapper.UpmsRoleMapper;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.service.UpmsRoleService;

@Service("upmsRoleService")
public class UpmsRoleServiceImpl implements UpmsRoleService {

	@Autowired
	private UpmsRoleMapper upmsRoleMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createRole(UpmsRole role) {
		ValidationAssert.notNull(role, "参数不能为空!");
		ValidationAssert.notEmpty(role.getRoleName(), "角色名称不能为空!");
		ValidationAssert.notEmpty(role.getRoleCode(), "角色代码不能为空!");
		role.setRoleId(null);
		role.setRoleType(UpmsRoleTypeEnum.ROLE_TYPE_NORMAL.getTypeCode());
		role.setCreateBy(ObjectUtils.defaultIfNull(role.getCreateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		role.setCreateTime(DateTimeUtils.formatNow());
		try {
			upmsRoleMapper.insertModel(role);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_NAME"), "新增角色失败,该角色名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_CODE"), "新增角色失败,该角色代码已经存在!");
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateRole(UpmsRole role) {
		ValidationAssert.notNull(role, "参数不能为空!");
		ValidationAssert.notNull(role.getRoleId(), "角色ID不能为空!");
		ValidationAssert.notEmpty(role.getRoleName(), "角色名称不能为空!");
		ValidationAssert.notEmpty(role.getRoleCode(), "角色代码不能为空!");
		role.setUpdateBy(ObjectUtils.defaultIfNull(role.getUpdateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		role.setUpdateTime(DateTimeUtils.formatNow());
		UpmsRole prole = upmsRoleMapper.selectModelById(role.getRoleId());
		ValidationAssert.notNull(prole, "该角色已经不存在了!");
		try {
			Map<String,Object> paramMap = role.mapBuilder().withRoleName().withRoleCode().withDescription().withUpdateBy().withUpdateTime().build();
			upmsRoleMapper.updateModelById(role.getRoleId(), paramMap);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_NAME"), "修改角色失败,该角色名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_CODE"), "修改角色失败,该角色代码已经存在!");
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteRoleById(Long roleId) {
		ValidationAssert.notNull(roleId, "角色ID不能为空!");
		UpmsRole prole = upmsRoleMapper.selectModelById(roleId);
		ValidationAssert.notNull(prole, "该角色已经不存在了!");
		BusinessAssert.isTrue(!UpmsRoleTypeEnum.ROLE_TYPE_SYSTEM.getTypeCode().equals(prole.getRoleType()), "删除角色失败,系统角色不允许删除!");
		Integer userRoles = upmsRoleMapper.selectUserRoleCountByRoleId(roleId);
		BusinessAssert.isTrue(userRoles == 0, "删除角色失败,该角色已经在使用不允许删除!");
		upmsRoleMapper.deleteModelById(roleId); //删除角色信息
		upmsRoleMapper.deleteRoleResourcesByRoleId(roleId); //删除该角色下的所有资源关系
	}

	@Override
	public UpmsRole getRoleById(Long roleId) {
		return ModelDecodeUtils.decodeModel(upmsRoleMapper.selectModelById(roleId));
	}

	@Override
	public List<UpmsRole> getRoleListByPage(UpmsRole condition, Page page, Sort sort) {
		List<UpmsRole> dataList = ModelDecodeUtils.decodeModel(upmsRoleMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(upmsRoleMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<UpmsResource> getResourceListByRoleId(Long roleId) {
		return ModelDecodeUtils.decodeModel(upmsRoleMapper.selectResourceListByRoleId(roleId));
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void configRoleResources(Long roleId, List<Long> resourceIdList, Long createBy, String createTime) {
		UpmsRole role = upmsRoleMapper.selectModelById(roleId);
		ValidationAssert.notNull(role, "该角色已经不存在了!");
		upmsRoleMapper.deleteRoleResourcesByRoleId(roleId);
		if(!CollectionUtils.isEmpty(resourceIdList)){
			upmsRoleMapper.insertRoleResources(roleId, resourceIdList, createBy, createTime);
		}
	}

}
