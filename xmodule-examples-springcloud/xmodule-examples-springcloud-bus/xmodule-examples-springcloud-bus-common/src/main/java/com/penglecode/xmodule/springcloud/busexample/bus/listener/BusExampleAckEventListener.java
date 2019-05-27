package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.cloud.consts.SpringCloudApplicationConstants;
import com.penglecode.xmodule.common.util.JsonUtils;

public class BusExampleAckEventListener implements ApplicationListener<AckRemoteApplicationEvent> {
	
	@Override
	public void onApplicationEvent(AckRemoteApplicationEvent event) {
		String busId = SpringCloudApplicationConstants.SPRING_CLOUD_BUS_ID;
		if(!event.getOriginService().equals(busId)) { //忽略自己发向自己的ACK
			System.out.println(String.format("【ACK-Event】>>> ServiceId [%s] listeners on, event = %s", busId, JsonUtils.object2Json(event)));
		}
	}

}
