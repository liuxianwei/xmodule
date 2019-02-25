package com.penglecode.xmodule.common.web.security.oauth2;

import org.springframework.context.support.MessageSourceAccessor;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.StringUtils;

public class OAuth2ErrorMessageSource {

	public static final OAuth2ErrorMessageSource INSTANCE = new OAuth2ErrorMessageSource();
	
	private String errorMessageCodePrefix = "spring.security.oauth2.error";

	protected MessageSourceAccessor getMessageSourceAccessor() {
		return ApplicationConstants.MESSAGE_SOURCE_ACCESSOR;
	}
	
	public String getErrorMessageCodePrefix() {
		return errorMessageCodePrefix;
	}

	public void setErrorMessageCodePrefix(String errorMessageCodePrefix) {
		this.errorMessageCodePrefix = StringUtils.stripEnd(errorMessageCodePrefix, ".");
	}
	
	/**
	 * 获取错误信息
	 * @param oauth2ErrorCode		- 不包含前缀的错误代码,例如：invalid_token, access_denied
	 * @param args
	 * @return
	 */
	public String getErrorMessage(String oauth2ErrorCode, Object[] args) {
		String messageCode = errorMessageCodePrefix + "." + oauth2ErrorCode;
		return getMessageSourceAccessor().getMessage(messageCode, args, (String)null);
	}

}
