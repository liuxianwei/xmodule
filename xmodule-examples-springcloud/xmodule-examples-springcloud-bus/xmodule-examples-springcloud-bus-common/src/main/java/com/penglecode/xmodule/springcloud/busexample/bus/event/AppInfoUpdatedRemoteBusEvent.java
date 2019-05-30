package com.penglecode.xmodule.springcloud.busexample.bus.event;

import java.util.Map;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import com.penglecode.xmodule.common.support.VersionedObject;
import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;

public class AppInfoUpdatedRemoteBusEvent extends RemoteApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final VersionedObject<Map<Long,AppInfo>> allAppInfos;
	
	public AppInfoUpdatedRemoteBusEvent() {
		super();
		this.allAppInfos = null;
	}

	public AppInfoUpdatedRemoteBusEvent(VersionedObject<Map<Long,AppInfo>> allAppInfos) {
		super();
		this.allAppInfos = allAppInfos;
	}

	public AppInfoUpdatedRemoteBusEvent(Object source, String originService, VersionedObject<Map<Long,AppInfo>> allAppInfos) {
		super(source, originService);
		this.allAppInfos = allAppInfos;
	}

	public AppInfoUpdatedRemoteBusEvent(Object source, String originService, String destinationService, VersionedObject<Map<Long,AppInfo>> allAppInfos) {
		super(source, originService, destinationService);
		this.allAppInfos = allAppInfos;
	}

	public VersionedObject<Map<Long,AppInfo>> getAllAppInfos() {
		return allAppInfos;
	}
	
}
