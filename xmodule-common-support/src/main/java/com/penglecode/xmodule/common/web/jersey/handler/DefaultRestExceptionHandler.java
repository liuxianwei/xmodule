package com.penglecode.xmodule.common.web.jersey.handler;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.consts.ContentType;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver.ExceptionMetadata;

@Component
@Provider
public class DefaultRestExceptionHandler implements ExceptionMapper<Exception> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultRestExceptionHandler.class);
	
	public Response toResponse(Exception e) {
		logger.error(e.getMessage(), e);
		ExceptionMetadata em = ModuleExceptionResolver.resolveException(e);
		Result<Object> result = Result.failure().code(em.getCode()).message(em.getMessage()).build();
		int statusCode = 200;
		if(e instanceof NotFoundException) {
			statusCode = 404;
		}
		return Response.ok(result, ContentType.APPLICATION_JSON).status(statusCode).build();
	}

}
