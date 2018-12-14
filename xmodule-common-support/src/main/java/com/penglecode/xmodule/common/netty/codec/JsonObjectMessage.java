package com.penglecode.xmodule.common.netty.codec;

import java.io.Serializable;

/**
 * 基于JSON的Java对象消息
 * 
 * Java对象通过JSON来传输(Java对象 -> 字符串 -> 字节流，字节流 -> 字符串 -> Java对象)，
 * 在字符串与想要的Java之间执行互相转换的协议
 * 
 * @param <T>
 * @author 	pengpeng
 * @date	2018年12月14日 下午12:48:40
 */
public class JsonObjectMessage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 对方想要的消息数据的Java类型
	 * 例如：java.util.List<com.xyz.bean.User>
	 */
	private String messageType;
	
	/**
	 * 消息数据
	 */
	private T messageData;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public T getMessageData() {
		return messageData;
	}

	public void setMessageData(T messageData) {
		this.messageData = messageData;
	}

}
