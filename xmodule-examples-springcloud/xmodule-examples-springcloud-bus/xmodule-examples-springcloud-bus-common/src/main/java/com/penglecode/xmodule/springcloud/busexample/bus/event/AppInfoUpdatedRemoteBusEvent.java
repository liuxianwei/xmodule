package com.penglecode.xmodule.springcloud.busexample.bus.event;

import java.util.Map;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;

public class AppInfoUpdatedRemoteBusEvent extends RemoteApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final Map<Long,AppInfo> allAppInfos;
	
	public AppInfoUpdatedRemoteBusEvent(Map<Long,AppInfo> allAppInfos) {
		super();
		this.allAppInfos = allAppInfos;
	}

	public AppInfoUpdatedRemoteBusEvent(Object source, String originService, Map<Long,AppInfo> allAppInfos) {
		super(source, originService);
		this.allAppInfos = allAppInfos;
	}

	public AppInfoUpdatedRemoteBusEvent(Object source, String originService, String destinationService, Map<Long,AppInfo> allAppInfos) {
		super(source, originService, destinationService);
		this.allAppInfos = allAppInfos;
	}

	public Map<Long, AppInfo> getAllAppInfos() {
		return allAppInfos;
	}
	
}
