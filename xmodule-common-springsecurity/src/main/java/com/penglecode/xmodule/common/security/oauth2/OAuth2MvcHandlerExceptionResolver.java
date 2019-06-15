package com.penglecode.xmodule.common.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver.ExceptionMetadata;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.springmvc.handler.DefaultMvcHandlerExceptionResolver;

/**
 * OAuth2认证授权服务器的标准MVC异常处理器
 * 
 * @author 	pengpeng
 * @date	2019年2月18日 下午12:32:33
 */
public class OAuth2MvcHandlerExceptionResolver extends DefaultMvcHandlerExceptionResolver {

	private OAuth2ErrorMessageSource oauth2ErrorMessageSource = OAuth2ErrorMessageSource.INSTANCE;
	
	@Override
	protected ModelAndView handle4AsyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		Result<Object> result = null;
		String message = null;
		int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		if(ex instanceof OAuth2Exception) {
			String exMessage = null;
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
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			message = oauth2ErrorMessageSource.getErrorMessage("unsupported_http_method", new Object[] {});
			message = StringUtils.defaultIfEmpty(message, "Unsupported Http Method");
			code = HttpStatus.METHOD_NOT_ALLOWED.value();
		} else if (ex instanceof MissingServletRequestParameterException) {
			MissingServletRequestParameterException requestException = (MissingServletRequestParameterException) ex;
			message = oauth2ErrorMessageSource.getErrorMessage("missing_request_param", new Object[] {requestException.getParameterName()});
			message = StringUtils.defaultIfEmpty(message, String.format("Missing Http Request Parameter: %s", requestException.getParameterName()));
			code = HttpStatus.BAD_REQUEST.value();
		} else {
			ExceptionMetadata em = ModuleExceptionResolver.resolveException(ex);
			message = em.getMessage();
		}
		result = Result.failure().code(String.valueOf(code)).message(message).build();
		response.setStatus(code);
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setExtractValueFromSingleKeyModel(true);
		ModelAndView mav = new ModelAndView(jsonView);
		mav.addObject(result);
		return mav;
	}

	public OAuth2ErrorMessageSource getOauth2ErrorMessageSource() {
		return oauth2ErrorMessageSource;
	}

	public void setOauth2ErrorMessageSource(OAuth2ErrorMessageSource oauth2ErrorMessageSource) {
		this.oauth2ErrorMessageSource = oauth2ErrorMessageSource;
	}
	
}
