package com.penglecode.xmodule.common.web.shiro.support;

import org.springframework.context.ApplicationEvent;

/**
 * Shiro权限配置信息(用户-角色-资源)变更的同步Event
 * 
 * @author 	pengpeng
 * @date	2018年6月4日 上午9:50:11
 */
public class ShiroPermsSynchronizeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ShiroPermsSynchronizeEvent(Object source) {
		super(source);
	}

}
