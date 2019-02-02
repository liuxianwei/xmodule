package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.model.UpmsApp;
import com.penglecode.xmodule.upms.service.UpmsAppService;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * UPMS应用接口
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午1:52:37
 */
@RestController
@RequestMapping("/app")
public class UpmsAppController extends HttpAPIResourceSupport {

	@Autowired
	private UpmsAppService upmsAppService;
	
	/**
	 * 查询应用列表(排序)
	 * @param condition		- 查询条件参数
	 * @param sort			- 排序参数
	 * @return
	 */
	@GetMapping(value="/list", produces=APPLICATION_JSON)
	public Result<List<UpmsApp>> getAppList(UpmsApp condition, Sort sort) {
		List<UpmsApp> dataList = upmsAppService.getAppList(condition, sort);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 查询全部应用列表
	 * @param enabled	- 是否启用(可选)
	 * @return
	 */
	@GetMapping(value="/all", produces=APPLICATION_JSON)
	public Result<List<UpmsApp>> getAllAppList(Boolean enabled) {
		List<UpmsApp> dataList = upmsAppService.getAllAppList(enabled);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 根据ID获取应用信息
	 * @param appId
	 * @return
	 */
	@GetMapping(value="/{appId}", produces=APPLICATION_JSON)
	public Result<UpmsApp> getAppById(@PathVariable("appId") Long appId) {
		UpmsApp app = upmsAppService.getAppById(appId);
		return Result.success().message("OK").data(app).build();
	}
	
	/**
	 * 创建应用
	 * @param app
	 * @return
	 */
	@PostMapping(value="/create", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> createApp(@RequestBody UpmsApp app) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		app.setCreateBy(loginUser.getUserId());
		upmsAppService.createApp(app);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新应用
	 * @param app
	 * @return
	 */
	@PutMapping(value="/update", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> updateApp(@RequestBody UpmsApp app) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		app.setCreateBy(loginUser.getUserId());
		upmsAppService.createApp(app);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 启用应用
	 * @param appId
	 * @return
	 */
	@PutMapping(value="/enable/{appId}", produces=APPLICATION_JSON)
	public Result<Object> enableApp(@PathVariable("appId") Long appId) {
		return updateAppStatus(appId, Boolean.TRUE);
	}
	
	/**
	 * 禁用应用
	 * @param appId
	 * @return
	 */
	@PutMapping(value="/disable/{appId}", produces=APPLICATION_JSON)
	public Result<Object> disableApp(@PathVariable("appId") Long appId) {
		return updateAppStatus(appId, Boolean.FALSE);
	}
	
	protected Result<Object> updateAppStatus(Long appId, Boolean targetStatus){
		upmsAppService.updateAppStatus(appId, targetStatus);
		return Result.success().message((targetStatus ? "启用" : "禁用") + "成功!").build();
	}
	
	/**
	 * 根据应用ID删除应用
	 * @param appId
	 * @return
	 */
	@DeleteMapping(value="/delete/{appId}", produces=APPLICATION_JSON)
	public Result<Object> deleteAppById(@PathVariable("appId") Long appId) {
		upmsAppService.deleteAppById(appId);
		return Result.success().message("删除成功!").build();
	}
	
	/**
	 * 自动生成UUID格式的应用KEY和应用密钥
	 * @return
	 */
	@GetMapping(value="/uuid/random", produces=APPLICATION_JSON)
	public Result<String> randomUUID() {
		return Result.success().message("OK").data(UUIDUtils.uuid()).build();
	}
	
}
