package com.penglecode.xmodule.common.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver.ExceptionMetadata;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.WebUtils;

/**
 * 默认的以{@code #Result}对象输出响应的HttpAccessExceptionHandler
 * 
 * @author 	pengpeng
 * @date	2019年2月14日 下午3:00:58
 */
public class DefaultHttpAccessExceptionHandler extends AbstractHttpAccessExceptionHandler {

	@Override
	protected ResponseEntity<?> createResponseEntity(HttpServletRequest request, HttpServletResponse response,
			Exception exception, Object attachment) throws Exception {
		ExceptionMetadata exceptionMetadata = ModuleExceptionResolver.resolveException(exception);
		Result<Object> result = Result.failure().code(exceptionMetadata.getCode()).message(exceptionMetadata.getMessage()).build();
		HttpHeaders headers = WebUtils.getHeaders(request);
		return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
	}

}
