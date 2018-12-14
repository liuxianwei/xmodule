package com.penglecode.xmodule.common.zookeeper;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

public class ZooKeeperException extends ApplicationRuntimeException {

	private static final long serialVersionUID = 1L;

	public ZooKeeperException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public ZooKeeperException(String code, String message) {
		super(code, message);
	}

	public ZooKeeperException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZooKeeperException(String message) {
		super(message);
	}

	public ZooKeeperException(Throwable cause) {
		super(cause);
	}

}
