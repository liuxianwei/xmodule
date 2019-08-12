package com.penglecode.xmodule.common.boot.actuator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * 可认为控制的HealthIndicator
 * 
 * @author 	pengpeng
 * @date	2018年9月20日 下午1:53:40
 */
public abstract class ControllableHealthIndicator extends AbstractHealthIndicator {

	private volatile boolean forceAppOffline = false;
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		if(forceAppOffline) { //强制应用下线
			builder.down().withDetail("error", "The application has been forced offline");
		} else { //否则通过健康检测逻辑来决定应用的上下线
			checkHealth(builder);
		}
	}

	protected abstract void checkHealth(Builder builder);
	
	public boolean isForceAppOffline() {
		return forceAppOffline;
	}

	public void setForceAppOffline(boolean forceAppOffline) {
		this.forceAppOffline = forceAppOffline;
	}

}
