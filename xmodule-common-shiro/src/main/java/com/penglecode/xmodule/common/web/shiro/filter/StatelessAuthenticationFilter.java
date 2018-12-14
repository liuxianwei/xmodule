package com.penglecode.xmodule.common.web.shiro.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penglecode.xmodule.common.exception.ApplicationBusinessException;
import com.penglecode.xmodule.common.exception.ApplicationException;
import com.penglecode.xmodule.common.exception.IllegalDigitalSignatureException;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver.ExceptionMetadata;
import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.util.HttpServletUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.NumberUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.shiro.authc.StatelessAuthenticationToken;

/**
 * 无状态Web应用认证过滤器
 * 
 * 无状态Web应用：客户端(HttpClient, Android, IOS)每次请求都会带上必要的参数(appKey, token, timestamp, signstr等)并且有一个私钥，
 * 客户端对appKey、token、timestamp使用密钥进行加密生成client signstr，服务端同样对其进行加密，生成server signstr，两者一匹配来验证请求的合法性。
 * 
 * @author 	pengpeng
 * @date	2018年1月4日 上午11:39:05
 */
public class StatelessAuthenticationFilter extends AccessControlFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);
	
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		try {
			return doOnAccessDenied(request, response);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			onAccessException(request, response, e);
			return false;
		}
	}
	
	protected boolean doOnAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//1、根据客户端请求参数生成无状态Token
		StatelessAuthenticationToken token = getAuthTokenFromRequest(request);
		//2、委托给Realm进行登录
		try {
			getSubject(request, response).login(token);
			return true;
		} catch (IncorrectCredentialsException e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalDigitalSignatureException("不合法的数字签名!");
		} catch (UnknownAccountException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ApplicationBusinessException("无效的appKey!");
		} catch (LockedAccountException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ApplicationBusinessException("该appKey被锁定!");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			if(e instanceof ApplicationException) {
				throw e;
			} else {
				throw new ApplicationBusinessException(String.format("认证出错!(错误信息:%s)", ExceptionUtils.getRootCauseMessage(e)));
			}
		}
	}
	
	protected StatelessAuthenticationToken getAuthTokenFromRequest(ServletRequest request) throws Exception {
		StatelessAuthenticationToken authToken = new StatelessAuthenticationToken();
		authToken.setRespondTime(System.currentTimeMillis());
		
		String appKey = getHeaderParam(request, "appKey");
		ValidationAssert.notEmpty(appKey, "请求头参数[appKey]不能为空!");
		String token = getHeaderParam(request, "token");
		ValidationAssert.notEmpty(token, "请求头参数[token]不能为空!");
		String timestampstr = getHeaderParam(request, "timestamp");
		ValidationAssert.notEmpty(timestampstr, "请求头参数[timestamp]不能为空!");
		ValidationAssert.isTrue(NumberUtils.isDigits(timestampstr), "请求头参数[timestamp]不合法!");
		Long timestamp = Long.valueOf(timestampstr);
		String signParamNameStr = getHeaderParam(request, "signParams");
		String signstr = getHeaderParam(request, "signstr");
		ValidationAssert.notEmpty(timestampstr, "请求头参数[signstr]不能为空!");
		
		Map<String,String> signParams = null;
		if(!StringUtils.isEmpty(signParamNameStr)) {
			signParams = new HashMap<String,String>();
			String[] signParamNames = signParamNameStr.split(",");
			for(String paramName : signParamNames) {
				signParams.put(paramName, WebUtils.getCleanParam(request, paramName));
			}
		}
		
		authToken.setAppKey(appKey);
		authToken.setToken(token);
		authToken.setTimestamp(timestamp);
		authToken.setSignstr(signstr);
		authToken.setSignParams(signParams);
		return authToken;
	}

	protected void onAccessException(ServletRequest request, ServletResponse response, Exception e) {
		try {
			ExceptionMetadata em = ModuleExceptionResolver.resolveException(e);
			Result<Object> result = Result.failure().code(em.getCode()).message(em.getMessage()).build();
			int httpStatus = HttpServletResponse.SC_OK;
			if(e instanceof IllegalDigitalSignatureException) {
				httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
			}
			HttpServletResponse res = WebUtils.toHttp(response);
			res.setStatus(httpStatus);
			HttpServletUtils.asynOutputResponse(res, ContentType.APPLICATION_JSON, JsonUtils.object2Json(result));
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}
	}
	
	protected String getHeaderParam(ServletRequest request, String paramName) {
		HttpServletRequest req = (HttpServletRequest) request;
		String paramValue = req.getHeader(paramName);
		return StringUtils.trim(paramValue);
	}
	
}
