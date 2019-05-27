package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.cloud.consts.SpringCloudApplicationConstants;
import com.penglecode.xmodule.springcloud.busexample.bus.event.ConsumerStartedRemoteBusEvent;

/**
 * Consumer服务启动起来了,向Bus发送ConsumerStartedRemoteBusEvent事件
 * 
 * @author 	pengpeng
 * @date	2019年5月25日 下午5:25:14
 */
public class ConsumerStartedEventListener implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if(event.getApplicationContext().equals(applicationContext)) {
			String busId = SpringCloudApplicationConstants.SPRING_CLOUD_BUS_ID;
			System.out.println(String.format("【Consumer-Started-Event】>>> 消费者Consumer[%s]启动起来了，即将向生产者Producer发送通知......", busId));
			event.getApplicationContext().publishEvent(new ConsumerStartedRemoteBusEvent(this, busId));
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
