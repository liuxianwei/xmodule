package com.penglecode.xmodule.common.web.shiro.support;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Shiro权限配置信息变动同步事件发布者
 * 
 * @author 	pengpeng
 * @date	2018年6月4日 上午9:50:06
 */
public class ShiroPermsSynchronizeEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	/**
	 * 供客户端调用以发布事件
	 */
	public void publishPermsSynchronizeEvent() {
		ShiroPermsSynchronizeEvent event = new ShiroPermsSynchronizeEvent(this);
		applicationEventPublisher.publishEvent(event); //发布事件
	}

}
