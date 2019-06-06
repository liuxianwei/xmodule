package com.penglecode.xmodule.springcloud.busexample.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.cloud.consts.SpringCloudApplicationConstants;
import com.penglecode.xmodule.common.support.VersionedObject;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.springcloud.busexample.bus.event.AppInfoUpdatedRemoteBusEvent;
import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;

@Service("appInfoService")
public class AppInfoService implements AppInfoBusEventPublisher {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppInfoService.class);
	
	private static final VersionedObject<Map<Long,AppInfo>> ALL_APP_INFOS = new VersionedObject<>(UUIDUtils.uuid(), new LinkedHashMap<Long,AppInfo>());
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public void addAppInfo(AppInfo appInfo) {
		Assert.notNull(appInfo, "Parameter 'appInfo' must be required!");
		appInfo.setAppId(ObjectUtils.defaultIfNull(appInfo.getAppId(), System.currentTimeMillis()));
		appInfo.setAppKey(StringUtils.defaultIfEmpty(appInfo.getAppKey(), UUIDUtils.uuid()));
		appInfo.setAppSecret(StringUtils.defaultIfEmpty(appInfo.getAppSecret(), UUIDUtils.uuid()));
		appInfo.setCreateTime(DateTimeUtils.formatNow());
		ALL_APP_INFOS.getObject().put(appInfo.getAppId(), appInfo);
		ALL_APP_INFOS.setVersion(UUIDUtils.uuid());
		publishAppInfoUpdatedEvent();
	}
	
	public void updateAppInfoById(AppInfo appInfo) {
		Assert.notNull(appInfo, "Parameter 'appInfo' must be required!");
		Assert.notNull(appInfo.getAppId(), "Parameter 'appInfo.appId' must be required!");
		ALL_APP_INFOS.getObject().forEach((id, app) -> {
			if(appInfo.getAppId().equals(id)) {
				BeanUtils.copyProperties(appInfo, app);
			}
		});
		ALL_APP_INFOS.setVersion(UUIDUtils.uuid());
		publishAppInfoUpdatedEvent();
	}
	
	public void deleteAppInfoById(Long appId) {
		Assert.notNull(appId, "Parameter 'appId' must be required!");
		ALL_APP_INFOS.getObject().remove(appId);
		ALL_APP_INFOS.setVersion(UUIDUtils.uuid());
		publishAppInfoUpdatedEvent();
	}
	
	public void publishAppInfoUpdatedEvent() {
		String busId = SpringCloudApplicationConstants.SPRING_CLOUD_BUS_ID.value();
		String contextId = applicationContext.getId();
		LOGGER.info(">>> publish event，contextId = {}, busId = {}, allAppInfos = {}", contextId, busId, ALL_APP_INFOS.getObject());
		
		applicationContext.publishEvent(new AppInfoUpdatedRemoteBusEvent(this, busId, ALL_APP_INFOS));
	}
	
}
