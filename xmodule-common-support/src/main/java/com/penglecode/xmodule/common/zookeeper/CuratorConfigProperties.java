package com.penglecode.xmodule.common.zookeeper;

/**
 * Zookeeper客户端CuratorFramework配置
 * 
 * @author 	pengpeng
 * @date	2018年6月28日 下午4:16:33
 */
public class CuratorConfigProperties {

	private String connectString = "127.0.0.1:2181";
	
	private Integer maxRetries = Integer.MAX_VALUE;
	
	private Integer sessionTimeoutMs = 60000;
	
	private Integer connectionTimeoutMs = 30000;

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

	public Integer getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public Integer getConnectionTimeoutMs() {
		return connectionTimeoutMs;
	}

	public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}
	
}
