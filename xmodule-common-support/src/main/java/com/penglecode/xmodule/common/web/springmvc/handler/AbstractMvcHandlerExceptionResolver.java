package com.penglecode.xmodule.common.web.springmvc.handler;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationException;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
/**
 * MVC异常处理器基类
 * 
 * @author 	pengpeng
 * @date	2019年2月18日 下午12:11:50
 */
public abstract class AbstractMvcHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMvcHandlerExceptionResolver.class);
	
	private String defaultExceptionView;
	
	private Charset defaultCharset = Charset.forName(GlobalConstants.DEFAULT_CHARSET);
	
	public void setDefaultExceptionView(String defaultExceptionView) {
		this.defaultExceptionView = defaultExceptionView;
	}
	
	public String getDefaultExceptionView() {
		return defaultExceptionView;
	}

	public Charset getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mav = null;
		try {
			if(!(ex instanceof ApplicationException)){
				LOGGER.error(ex.getMessage(), ex); //未知异常记录下来
			}
			if(handler instanceof HandlerMethod){
				if(isAsyncRequest(request, response, handler)){ //异步请求的异常处理
					return handle4AsyncRequest(request, response, handler, ex);
				} else { //正常页面跳转的同步请求的异常处理
					return handle4SyncRequest(request, response, handler, ex);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
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
		return SpringWebMvcUtils.isAsyncRequest(request, handler);
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
