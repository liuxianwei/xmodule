package com.penglecode.xmodule.myexample.web.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class SmartLifecycleBean implements SmartLifecycle, InitializingBean {

	private volatile boolean running = false;

	@Override
	public void start() {
		running = true;
		System.out.println(">>> start");
	}

	@Override
	public void stop() {
		running = false;
		System.out.println("<<< stop");
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(">>> init");
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		
	}
	
}
