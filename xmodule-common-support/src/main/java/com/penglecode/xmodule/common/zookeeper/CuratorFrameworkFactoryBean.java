package com.penglecode.xmodule.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * ZooKeeper客户端CuratorFramework的工厂Bean
 * 
 * @author 	pengpeng
 * @date	2018年6月28日 下午4:24:29
 */
public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework>, DisposableBean {

	private CuratorConfigProperties curatorConfig;
	
	private CuratorFramework zooKeeperClient;
	
	@Override
	public CuratorFramework getObject() throws Exception {
		zooKeeperClient = createCuratorFramework();
		zooKeeperClient.start();
		zooKeeperClient.blockUntilConnected();
		return zooKeeperClient;
	}
	
	protected CuratorFramework createCuratorFramework() {
		/**
		 * 指数形式重连接策略：重试指定的次数, 且每一次重试之间停顿的时间逐渐增加。
		 */
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, curatorConfig.getMaxRetries());
		return CuratorFrameworkFactory.builder().connectString(curatorConfig.getConnectString())
				.retryPolicy(retryPolicy).connectionTimeoutMs(curatorConfig.getConnectionTimeoutMs())
				.sessionTimeoutMs(curatorConfig.getSessionTimeoutMs()).build();
	}

	@Override
	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public CuratorConfigProperties getCuratorConfig() {
		return curatorConfig;
	}

	public void setCuratorConfig(CuratorConfigProperties curatorConfig) {
		this.curatorConfig = curatorConfig;
	}

	protected CuratorFramework getZooKeeperClient() {
		return zooKeeperClient;
	}

	@Override
	public void destroy() throws Exception {
		if(!CuratorFrameworkState.STOPPED.equals(zooKeeperClient.getState())) {
			zooKeeperClient.close();
		}
	}

}
