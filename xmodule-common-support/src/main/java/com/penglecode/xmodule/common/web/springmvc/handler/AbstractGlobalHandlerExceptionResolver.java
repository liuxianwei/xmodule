package com.penglecode.xmodule.common.web.springmvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.penglecode.xmodule.common.exception.ApplicationException;
import com.penglecode.xmodule.common.util.SpringMvcUtils;
/**
 * 全局异常处理器
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月3日 下午9:56:01
 * @version  	1.0
 */
public abstract class AbstractGlobalHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGlobalHandlerExceptionResolver.class);
	
	private String defaultExceptionView;
	
	public void setDefaultExceptionView(String defaultExceptionView) {
		this.defaultExceptionView = defaultExceptionView;
	}
	
	public String getDefaultExceptionView() {
		return defaultExceptionView;
	}

	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mav = null;
		try {
			if(!(ex instanceof ApplicationException)){
				logger.error(ex.getMessage(), ex); //未知异常记录下来
			}
			if(handler instanceof HandlerMethod){
				if(isAsyncRequest(request, response, handler)){ //异步请求的异常处理
					return handle4AsyncRequest(request, response, handler, ex);
				} else { //正常页面跳转的同步请求的异常处理
					return handle4SyncRequest(request, response, handler, ex);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mav;
	}
	
	/**
	 * 是否是异步请求
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 */
	protected boolean isAsyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler){
		return SpringMvcUtils.isAsyncRequest(request, handler);
	}
	
	/**
	 * 处理异步请求
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @return
	 */
	protected abstract ModelAndView handle4AsyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
	
	/**
	 * 处理同步请求
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @return
	 */
	protected abstract ModelAndView handle4SyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}
