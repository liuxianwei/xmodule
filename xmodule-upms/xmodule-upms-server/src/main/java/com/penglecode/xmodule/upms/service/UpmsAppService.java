package com.penglecode.xmodule.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.upms.model.UpmsApp;

/**
 * UPMS应用服务
 * 
 * @author 	pengpeng
 * @date	2018年4月23日 下午4:01:34
 */
public interface UpmsAppService {

	/**
	 * 查询应用列表(排序)
	 * @param condition
	 * @param sort
	 * @return
	 */
	public List<UpmsApp> getAppList(UpmsApp condition, Sort sort);
	
	/**
	 * 获取所有应用列表
	 * @param enabled	- 为null则查询所有
	 * @return
	 */
	public List<UpmsApp> getAllAppList(Boolean enabled);

	/**
	 * 创建应用
	 * @param app
	 */
	public void createApp(UpmsApp app);
	
	/**
	 * 创建应用
	 * @param app
	 */
	public void updateApp(UpmsApp app);
	
	/**
	 * 修改应用状态
	 * @param appId
	 * @param targetStatus
	 */
	public void updateAppStatus(Long appId, boolean targetStatus);
	
	/**
     * 根据应用ID删除应用信息
     * @param appId
     */
    public void deleteAppById(Long appId);
	
}
