package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import java.util.Map;

import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.cloud.consts.SpringCloudApplicationConstants;
import com.penglecode.xmodule.common.support.VersionedData;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.springcloud.busexample.bus.event.AppInfoUpdatedRemoteBusEvent;
import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;

public class AppInfoUpdatedRemoteBusEventListener implements ApplicationListener<AppInfoUpdatedRemoteBusEvent> {

	private volatile VersionedData<Map<Long,AppInfo>> allAppInfos;
	
	@Override
	public void onApplicationEvent(AppInfoUpdatedRemoteBusEvent event) {
		String busId = SpringCloudApplicationConstants.SPRING_CLOUD_BUS_ID;
		boolean dataChanged = allAppInfos == null || !allAppInfos.getVersion().equals(event.getAllAppInfos().getVersion());
		if(dataChanged) {
			this.allAppInfos = event.getAllAppInfos();
		}
		System.out.println(String.format("【AppInfo-Updated-Remote-Event】>>> ServiceId [%s] listeners on, dataChanged = %s, event = %s", busId, dataChanged, JsonUtils.object2Json(event)));
	}

}