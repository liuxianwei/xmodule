package com.penglecode.xmodule.springcloud.busexample.bus.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 消费者服务启动起来了,向生产者发送通知,生产者广播最新数据值
 * 
 * @author 	pengpeng
 * @date	2019年5月25日 下午4:55:41
 */
public class ConsumerStartedRemoteBusEvent extends RemoteApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private static final String PRODUCER_APPLICATION_NAME = "springcloud-bus-producer";

	public ConsumerStartedRemoteBusEvent() {
		super();
	}

	public ConsumerStartedRemoteBusEvent(Object source, String originService) {
		super(source, originService, PRODUCER_APPLICATION_NAME + ":*"); //只向producer发送消息
	}

}
