package com.penglecode.xmodule.springcloud.nacosexample.config;

public class NacosConfigProperties {

	private String serverAddr;
	
	private String fileExtension;
	
	private Long timeout = 5000L;

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	
}
