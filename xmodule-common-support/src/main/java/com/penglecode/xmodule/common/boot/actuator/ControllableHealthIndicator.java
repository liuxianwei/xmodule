package com.penglecode.xmodule.common.boot.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * 可认为控制的HealthIndicator
 * 
 * @author 	pengpeng
 * @date	2018年9月20日 下午1:53:40
 */
public abstract class ControllableHealthIndicator implements HealthIndicator {

	private volatile boolean forceHealthDown = false;
	
	@Override
	public final Health health() {
		Health health = null;
		if(isForceHealthDown()) { //强制应用下线
			health = onApplicationOffline();
		} else { //强制应用上线
			health = onApplicationOnline();
		}
		return health;
	}
	
	/**
	 * 强制应用上线，但是应用到底能不能上线还可以通过覆盖该方法进行自定义
	 * @return
	 */
	protected Health onApplicationOnline() {
		return Health.up().build();
	}
	
	/**
	 * 强制应用下线，一旦强制下线那么久真的下线了
	 * @return
	 */
	protected Health onApplicationOffline() {
		return Health.down().withDetail("error", "The application has been forced offline").build();
	}

	public boolean isForceHealthDown() {
		return forceHealthDown;
	}

	public void setForceHealthDown(boolean forceHealthDown) {
		this.forceHealthDown = forceHealthDown;
	}

}
