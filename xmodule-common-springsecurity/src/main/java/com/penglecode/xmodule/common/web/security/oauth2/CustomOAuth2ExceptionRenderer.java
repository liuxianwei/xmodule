package com.penglecode.xmodule.common.web.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;

import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.AbstractHttpAccessExceptionHandler;

/**
 * 自定义的令牌异常响应渲染
 * 
 * @author 	pengpeng
 * @date	2019年2月14日 下午4:50:32
 */
public class CustomOAuth2ExceptionRenderer extends AbstractHttpAccessExceptionHandler implements OAuth2ExceptionRenderer, InitializingBean {

	private OAuth2ErrorMessageSource oauth2ErrorMessageSource = OAuth2ErrorMessageSource.INSTANCE;
	
	@Override
	public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest)
			throws Exception {
		if (responseEntity != null && responseEntity instanceof ResponseEntity) {
			handleException(webRequest.getRequest(), webRequest.getResponse(), null, responseEntity);
		}
	}
	
	@Override
	protected ResponseEntity<?> createResponseEntity(HttpServletRequest request, HttpServletResponse response, Exception exception, Object attachment) throws Exception {
		ResponseEntity<?> responseEntity = (ResponseEntity<?>) attachment;
		int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		String message = null;
		if(responseEntity.getBody() instanceof OAuth2Exception) {
			String exMessage = null;
			OAuth2Exception oauth2Exception = (OAuth2Exception) responseEntity.getBody();
			if(oauth2Exception.getHttpErrorCode() == HttpStatus.INTERNAL_SERVER_ERROR.value() && oauth2Exception.getCause() != null) { //500，服务器内部错误
				exMessage = ModuleExceptionResolver.resolveException(oauth2Exception.getCause()).getMessage();
				message = oauth2ErrorMessageSource.getErrorMessage(oauth2Exception.getOAuth2ErrorCode(), new Object[] {exMessage});
			} else {
				exMessage = oauth2Exception.getMessage();
				message = oauth2ErrorMessageSource.getErrorMessage(oauth2Exception.getOAuth2ErrorCode(), new Object[] {});
			}
			code = oauth2Exception.getHttpErrorCode();
			message = StringUtils.defaultIfEmpty(message, oauth2Exception.getOAuth2ErrorCode() + ", " + exMessage);
		} else if (responseEntity.getBody() instanceof HttpRequestMethodNotSupportedException) {
			message = oauth2ErrorMessageSource.getErrorMessage("unsupported_http_method", new Object[] {});
			message = StringUtils.defaultIfEmpty(message, "Unsupported Http Method");
			code = HttpStatus.METHOD_NOT_ALLOWED.value();
		} else if (responseEntity.getBody() instanceof MissingServletRequestParameterException) {
			MissingServletRequestParameterException requestException = (MissingServletRequestParameterException) responseEntity.getBody();
			message = oauth2ErrorMessageSource.getErrorMessage("missing_request_param", new Object[] {requestException.getParameterName()});
			message = StringUtils.defaultIfEmpty(message, String.format("Missing Http Request Parameter: %s", requestException.getParameterName()));
			code = HttpStatus.BAD_REQUEST.value();
		} else {
			message = responseEntity.getBody() == null ? "unkown error" : responseEntity.getBody().toString();
		}
		Result<Object> result = Result.failure().code(String.valueOf(code)).message(message).build();
		response.setStatus(code);
		return new ResponseEntity<Object>(result, responseEntity.getHeaders(), responseEntity.getStatusCode());
	}

	public OAuth2ErrorMessageSource getOauth2ErrorMessageSource() {
		return oauth2ErrorMessageSource;
	}

	public void setOauth2ErrorMessageSource(OAuth2ErrorMessageSource oauth2ErrorMessageSource) {
		this.oauth2ErrorMessageSource = oauth2ErrorMessageSource;
	}

}