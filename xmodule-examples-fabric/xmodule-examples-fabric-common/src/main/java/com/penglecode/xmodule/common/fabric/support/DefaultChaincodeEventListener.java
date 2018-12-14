package com.penglecode.xmodule.common.fabric.support;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultChaincodeEventListener implements ChaincodeEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChaincodeEventListener.class);
	
	@Override
	public void received(String handle, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {
		LOGGER.info("【EVENT】>>> handle = {}, blockEvent = {}, chaincodeEvent = {}", handle, blockEvent, chaincodeEvent);
	}

}
