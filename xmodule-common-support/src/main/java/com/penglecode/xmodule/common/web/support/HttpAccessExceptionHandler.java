package com.penglecode.xmodule.common.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP访问异常处理器
 * 
 * @author 	pengpeng
 * @date	2019年2月14日 下午2:05:13
 */
public interface HttpAccessExceptionHandler {

	/**
	 * 处理访问异常并输出到客户端
	 * @param request
	 * @param response
	 * @param exception
	 * @param attachment
	 */
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception, Object attachment);
	
}
