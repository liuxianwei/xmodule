package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;

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
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceActionTypeEnum;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.service.UpmsResourceService;
import com.penglecode.xmodule.upms.support.UpmsResourceTreeBuilder;
import com.penglecode.xmodule.upms.support.UpmsResourceTreeNodeConverter;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * UPMS资源接口
 * 
 * @author 	pengpeng
 * @date	2019年1月22日 上午9:43:07
 */
@RestController
@RequestMapping("/resource")
public class UpmsResourceController extends HttpAPIResourceSupport {

	private AbstractXTreeBuilder<Long, UpmsResource> resourceTreeBuilder = new UpmsResourceTreeBuilder();
	
	private TreeNodeConverter<UpmsResource,Map<String,Object>> resourceTreeNodeConverter = new UpmsResourceTreeNodeConverter();
	
	@Autowired
	private UpmsResourceService upmsResourceService;
	
	/**
	 * 获取可用的资源树结构
	 * @param appId
	 * @param actionType
	 * @return
	 */
	@GetMapping(value="/list/{appId}", produces=APPLICATION_JSON)
	public Result<List<Map<String,Object>>> getAppResourceList(@PathVariable("appId") Long appId, Integer actionType) {
		if(actionType != null && UpmsResourceActionTypeEnum.getType(actionType) == null) {
			actionType = null;
		}
		List<UpmsResource> allResourceList = upmsResourceService.getResourceListByParam(appId, actionType, Boolean.TRUE);
		List<Map<String,Object>> dataList = resourceTreeBuilder.buildObjectTree(appId, allResourceList, resourceTreeNodeConverter);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 根据资源ID获取资源信息
	 * @param resourceId
	 * @return
	 */
	@GetMapping(value="/{resourceId}", produces=APPLICATION_JSON)
	public Result<UpmsRole> getResourceById(@PathVariable("resourceId") Long resourceId) {
		UpmsResource resource = upmsResourceService.getResourceById(resourceId);
		return Result.success().message("OK").data(resource).build();
	}
	
	/**
	 * 创建资源
	 * @param resource
	 * @return
	 */
	@PostMapping(value="/create", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> createRole(@RequestBody UpmsResource resource) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		resource.setCreateBy(loginUser.getUserId());
		upmsResourceService.createResource(resource);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新资源
	 * @param resource
	 * @return
	 */
	@PutMapping(value="/update", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> updateRole(@RequestBody UpmsResource resource) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		resource.setUpdateBy(loginUser.getUserId());
		upmsResourceService.updateResource(resource);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据资源ID删除资源
	 * @param resourceId
	 * @return
	 */
	@DeleteMapping(value="/delete/{resourceId}", produces=APPLICATION_JSON)
	public Result<Object> deleteResourceById(@PathVariable("resourceId") Long resourceId) {
		upmsResourceService.deleteResourceById(resourceId, Boolean.TRUE);
		return Result.success().message("删除成功!").build();
	}
	
}
