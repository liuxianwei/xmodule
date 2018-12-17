package com.penglecode.xmodule.common.fabric.support;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBlockListener implements BlockListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBlockListener.class);
	
	@Override
	public void received(BlockEvent event) {
		try {
			List<String> transactionIds = new ArrayList<String>();
			Iterable<EnvelopeInfo> envelopeInfos = event.getEnvelopeInfos();
			if(envelopeInfos != null) {
				for(EnvelopeInfo envelopeInfo : envelopeInfos) {
					transactionIds.add(envelopeInfo.getTransactionID());
				}
			}
			LOGGER.info(">>> 收到BlockEvent事件, channelId = {}, blockNumber = {}, transactionIds = {}", event.getChannelId(), event.getBlockNumber(), transactionIds);
			//LOGGER.info(">>> 收到BlockEvent事件, blockAllFields = {}", block.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
