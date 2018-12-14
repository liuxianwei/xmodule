package com.penglecode.xmodule.common.web.shiro.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.penglecode.xmodule.common.support.RedisConfigSynchronizer;
import com.penglecode.xmodule.common.util.UUIDUtils;

public class UpmsAppShiroPermsRedisConfigSynchronizer extends RedisConfigSynchronizer<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsAppShiroPermsRedisConfigSynchronizer.class);
	
	private static final String DEFAULT_PERMISSION_VERSION_REDIS_KEY = "shiro:upms_permission_sync";
	
	private ShiroPermsSynchronizeEventPublisher shiroPermsSynchronizeEventPublisher;
	
	public UpmsAppShiroPermsRedisConfigSynchronizer() {
		super();
		setConfigRedisKey(DEFAULT_PERMISSION_VERSION_REDIS_KEY);
	}

	@Override
	protected void updateLocalConfig(boolean configChanged, ConfigEvent configEvent, String configValue)
			throws Exception {
		try {
			LOGGER.info(">>> 重新加载应用的Shiro全局(URL-权限)配置");
			shiroPermsSynchronizeEventPublisher.publishPermsSynchronizeEvent();
		} catch (Exception e) {
			LOGGER.error(String.format(">>> 重新加载应用的Shiro全局(URL-权限)配置发生异常: %s", e.getMessage()), e);
		}
	}

	@Override
	protected String getInitialConfig() throws Exception {
		return UUIDUtils.uuid();
	}

	public ShiroPermsSynchronizeEventPublisher getShiroPermsSynchronizeEventPublisher() {
		return shiroPermsSynchronizeEventPublisher;
	}

	@Autowired
	public void setShiroPermsSynchronizeEventPublisher(
			ShiroPermsSynchronizeEventPublisher shiroPermsSynchronizeEventPublisher) {
		this.shiroPermsSynchronizeEventPublisher = shiroPermsSynchronizeEventPublisher;
	}

}
