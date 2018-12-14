package com.penglecode.xmodule.common.web.support;

import com.penglecode.xmodule.common.exception.ApplicationException;

public class HttpResubmitException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public HttpResubmitException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpResubmitException(String message) {
		super(message);
	}

}
