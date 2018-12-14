package com.penglecode.xmodule.common.web.support;

import java.util.Map;

import org.springframework.web.context.request.WebRequest;

public class DefaultErrorAttributes extends org.springframework.boot.web.servlet.error.DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		errorAttributes.put("exception", getError(webRequest));
		return errorAttributes;
	}

}
