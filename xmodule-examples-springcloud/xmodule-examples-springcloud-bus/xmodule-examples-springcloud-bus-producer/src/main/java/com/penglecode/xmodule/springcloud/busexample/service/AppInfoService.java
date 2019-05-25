package com.penglecode.xmodule.springcloud.busexample.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.springcloud.busexample.bus.event.AppInfoUpdatedRemoteBusEvent;
import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;

@Service("appInfoService")
public class AppInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppInfoService.class);
	
	private static final Map<Long,AppInfo> ALL_APP_INFOS = new LinkedHashMap<Long,AppInfo>();
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public void addAppInfo(AppInfo appInfo) {
		Assert.notNull(appInfo, "Parameter 'appInfo' must be required!");
		appInfo.setAppId(ObjectUtils.defaultIfNull(appInfo.getAppId(), System.currentTimeMillis()));
		appInfo.setAppKey(StringUtils.defaultIfEmpty(appInfo.getAppKey(), UUIDUtils.uuid()));
		appInfo.setAppSecret(StringUtils.defaultIfEmpty(appInfo.getAppSecret(), UUIDUtils.uuid()));
		appInfo.setCreateTime(DateTimeUtils.formatNow());
		ALL_APP_INFOS.put(appInfo.getAppId(), appInfo);
		publishAppInfoUpdatedEvent();
	}
	
	public void updateAppInfoById(AppInfo appInfo) {
		Assert.notNull(appInfo, "Parameter 'appInfo' must be required!");
		Assert.notNull(appInfo.getAppId(), "Parameter 'appInfo.appId' must be required!");
		ALL_APP_INFOS.forEach((id, app) -> {
			if(appInfo.getAppId().equals(id)) {
				BeanUtils.copyProperties(appInfo, app);
			}
		});
		publishAppInfoUpdatedEvent();
	}
	
	public void deleteAppInfoById(Long appId) {
		Assert.notNull(appId, "Parameter 'appId' must be required!");
		ALL_APP_INFOS.remove(appId);
		publishAppInfoUpdatedEvent();
	}
	
	protected void publishAppInfoUpdatedEvent() {
		LOGGER.info(">>> publish event，id = {}", applicationContext.getId());
		applicationContext.publishEvent(new AppInfoUpdatedRemoteBusEvent(this, applicationContext.getId(), ALL_APP_INFOS));
	}
	
}
