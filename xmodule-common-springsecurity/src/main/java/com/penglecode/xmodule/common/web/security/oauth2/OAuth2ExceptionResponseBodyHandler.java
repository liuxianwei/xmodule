package com.penglecode.xmodule.common.web.security.oauth2;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 针对OAuth2Exception的响应处理器
 * 
 * @author 	pengpeng
 * @date	2019年2月19日 上午11:17:10
 */
public class OAuth2ExceptionResponseBodyHandler implements ResponseBodyAdvice<Object> {

	private OAuth2ErrorMessageSource oauth2ErrorMessageSource = OAuth2ErrorMessageSource.INSTANCE;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body,
			MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if(body instanceof OAuth2Exception) {
			String tokenServiceRequestSource = request.getHeaders().getFirst(SecurityOAuth2Constants.OAUTH2_TOKEN_SERVICE_REQUEST_SOURCE);
			if(!RemoteTokenServices.class.getSimpleName().equals(tokenServiceRequestSource)) {
				OAuth2Exception ex = (OAuth2Exception) body;
				int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
				String message = null, exMessage = null;
				OAuth2Exception oauth2Exception = (OAuth2Exception) ex;
				if(oauth2Exception.getHttpErrorCode() == HttpStatus.INTERNAL_SERVER_ERROR.value() && oauth2Exception.getCause() != null) { //500，服务器内部错误
					exMessage = ModuleExceptionResolver.resolveException(oauth2Exception.getCause()).getMessage();
					message = oauth2ErrorMessageSource.getErrorMessage(oauth2Exception.getOAuth2ErrorCode(), new Object[] {exMessage});
				} else {
					exMessage = oauth2Exception.getMessage();
					message = oauth2ErrorMessageSource.getErrorMessage(oauth2Exception.getOAuth2ErrorCode(), new Object[] {});
				}
				message = StringUtils.defaultIfEmpty(message, exMessage);
				code = oauth2Exception.getHttpErrorCode();
				return Result.failure().code(String.valueOf(code)).message(message).build();
			}
		}
		return body;
	}

	public OAuth2ErrorMessageSource getOauth2ErrorMessageSource() {
		return oauth2ErrorMessageSource;
	}

	public void setOauth2ErrorMessageSource(OAuth2ErrorMessageSource oauth2ErrorMessageSource) {
		this.oauth2ErrorMessageSource = oauth2ErrorMessageSource;
	}

}
