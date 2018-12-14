package com.penglecode.xmodule.common.fabric.support;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBlockListener implements BlockListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBlockListener.class);
	
	@Override
	public void received(BlockEvent event) {
		LOGGER.info(">>> 收到BlockEvent事件, event = {}", event);
	}

}
