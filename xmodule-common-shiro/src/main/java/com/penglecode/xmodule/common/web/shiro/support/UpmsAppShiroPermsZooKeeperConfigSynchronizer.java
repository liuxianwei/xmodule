package com.penglecode.xmodule.common.web.shiro.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.ZooKeeperConfigSynchronizer;
import com.penglecode.xmodule.common.util.UUIDUtils;

public class UpmsAppShiroPermsZooKeeperConfigSynchronizer extends ZooKeeperConfigSynchronizer {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsAppShiroPermsZooKeeperConfigSynchronizer.class);
	
	private static final String DEFAULT_PERMISSION_RELOAD_ZK_PATH = "/shiro/permission_reload_version";
	
	private ShiroPermsSynchronizeEventPublisher shiroPermsSynchronizeEventPublisher;
	
	public UpmsAppShiroPermsZooKeeperConfigSynchronizer() {
		super();
		setConfigNodePath(DEFAULT_PERMISSION_RELOAD_ZK_PATH);
	}

	@Override
	protected void updateLocalConfig(boolean configChanged, ConfigEvent configEvent, byte[] configValue)
			throws Exception {
		try {
			LOGGER.info(">>> 重新加载系统的Shiro全局(URL-权限)配置");
			shiroPermsSynchronizeEventPublisher.publishPermsSynchronizeEvent();
		} catch (Exception e) {
			LOGGER.error(String.format(">>> 重新加载系统的Shiro全局(URL-权限)配置发生异常: %s", e.getMessage()), e);
		}
	}

	@Override
	protected byte[] getInitialConfig() throws Exception {
		return UUIDUtils.uuid().getBytes(GlobalConstants.DEFAULT_CHARSET);
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
