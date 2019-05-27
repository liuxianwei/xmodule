package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.springcloud.busexample.bus.event.ConsumerStartedRemoteBusEvent;
import com.penglecode.xmodule.springcloud.busexample.service.AppInfoBusEventPublisher;

public class ConsumerStartedRemoteBusEventListener implements ApplicationListener<ConsumerStartedRemoteBusEvent> {

	@Autowired
	private AppInfoBusEventPublisher appInfoBusEventPublisher;
	
	@Override
	public void onApplicationEvent(ConsumerStartedRemoteBusEvent event) {
		System.out.println(String.format("【Consumer-Started-Remote-Event】>>> 发现到消费者(%s)上线啦, 即将广播所有AppInfo的最新值!", event.getOriginService()));
		appInfoBusEventPublisher.publishAppInfoUpdatedEvent();
	}

}
