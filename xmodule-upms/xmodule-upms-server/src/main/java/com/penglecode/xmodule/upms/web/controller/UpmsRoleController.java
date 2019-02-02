package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.AbstractXTreeBuilder;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.service.UpmsResourceService;
import com.penglecode.xmodule.upms.service.UpmsRoleService;
import com.penglecode.xmodule.upms.support.UpmsResourceSimpleTreeNodeConverter;
import com.penglecode.xmodule.upms.support.UpmsResourceTreeBuilder;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * UPMS角色接口
 * 
 * @author 	pengpeng
 * @date	2019年1月22日 上午9:43:07
 */
@RestController
@RequestMapping("/role")
public class UpmsRoleController extends HttpAPIResourceSupport {

	private AbstractXTreeBuilder<Long, UpmsResource> resourceTreeBuilder = new UpmsResourceTreeBuilder();
	
	private TreeNodeConverter<UpmsResource,Map<String,Object>> resourceTreeNodeConverter = new UpmsResourceSimpleTreeNodeConverter();
	
	@Autowired
	private UpmsRoleService upmsRoleService;
	
	@Autowired
	private UpmsResourceService upmsResourceService;
	
	/**
	 * 查询角色列表(分页、排序)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	@GetMapping(value="/list", produces=APPLICATION_JSON)
	public Object getRoleList(UpmsRole condition, Page page, Sort sort) {
		List<UpmsRole> dataList = upmsRoleService.getRoleListByPage(condition, page, sort);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 根据角色ID获取角色信息
	 * @param roleId
	 * @return
	 */
	@GetMapping(value="/{roleId}", produces=APPLICATION_JSON)
	public Result<UpmsRole> getRoleById(@PathVariable("roleId") Long roleId) {
		UpmsRole role = upmsRoleService.getRoleById(roleId);
		return Result.success().message("OK").data(role).build();
	}
	
	/**
	 * 创建角色
	 * @param role
	 * @return
	 */
	@PostMapping(value="/create", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> createRole(@RequestBody UpmsRole role) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		role.setCreateBy(loginUser.getUserId());
		upmsRoleService.createRole(role);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新角色
	 * @param role
	 * @return
	 */
	@PutMapping(value="/update", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> updateRole(@RequestBody UpmsRole role) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		role.setUpdateBy(loginUser.getUserId());
		upmsRoleService.updateRole(role);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据角色ID删除角色
	 * @param roleId
	 * @return
	 */
	@DeleteMapping(value="/delete/{roleId}", produces=APPLICATION_JSON)
	public Result<Object> deleteUserById(@PathVariable("roleId") Long roleId) {
		upmsRoleService.deleteRoleById(roleId);
		return Result.success().message("删除成功!").build();
	}
	
	/**
	 * 查询角色-资源配置关系
	 * @param roleId
	 * @param appId
	 * @return
	 */
	@GetMapping(value="/resources/{appId}/{roleId}", produces=APPLICATION_JSON)
	public Result<Map<String,Object>> getRoleResources(@PathVariable("roleId") Long roleId, @PathVariable("appId") Long appId) {
		List<UpmsResource> allResourceList = upmsResourceService.getResourceListByParam(appId, null, Boolean.FALSE);
		List<UpmsResource> roleResourceList = upmsRoleService.getResourceListByRoleId(roleId);
		List<Long> checkedResourceIds = new ArrayList<Long>();
		if(!CollectionUtils.isEmpty(roleResourceList)){
			for(UpmsResource roleResource : roleResourceList){
				checkedResourceIds.add(roleResource.getResourceId());
			}
		}
		List<Map<String,Object>> allAppResourceList = resourceTreeBuilder.buildObjectTree(appId, allResourceList, resourceTreeNodeConverter);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("allAppResourceList", allAppResourceList);
		dataMap.put("checkedResourceIds", checkedResourceIds);
		return Result.success().message("OK").data(dataMap).build();
	}
	
	/**
	 * 配置角色-资源关系
	 * @param parameter
	 * @return
	 */
	@PutMapping(value="/resources/config", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> configRoleResources(@RequestBody Map<String,Object> parameter) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		List<Long> resourceIdList = new ArrayList<Long>();
		Long roleId = MapUtils.getLong(parameter, "roleId");
		String resourceIds = MapUtils.getString(parameter, "resourceIds");
		if(!StringUtils.isEmpty(resourceIds)){
			resourceIdList = Stream.of(resourceIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
		}
		upmsRoleService.configRoleResources(roleId, resourceIdList, loginUser.getUserId(), DateTimeUtils.formatNow());
		return Result.success().message("配置成功!").build();
	}
	
}
