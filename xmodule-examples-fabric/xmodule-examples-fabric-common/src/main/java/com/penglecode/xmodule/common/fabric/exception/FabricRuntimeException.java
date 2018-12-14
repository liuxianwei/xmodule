package com.penglecode.xmodule.common.fabric.exception;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

public class FabricRuntimeException extends ApplicationRuntimeException {

	private static final long serialVersionUID = 1L;

	public FabricRuntimeException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public FabricRuntimeException(String code, String message) {
		super(code, message);
	}

	public FabricRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FabricRuntimeException(String message) {
		super(message);
	}

	public FabricRuntimeException(Throwable cause) {
		super(cause);
	}

}
