package com.penglecode.xmodule.common.web.shiro.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;

/**
 * 基于URL的Shiro访问权限验证
 * 
 * @author 	pengpeng
 * @date	2018年6月7日 下午4:08:35
 */
public abstract class ClientUrlAccessFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientUrlAccessFilter.class);
	
	private String filterName;
	
	private PatternMatcher pathMatcher = new AntPathMatcher();
	
	private volatile Map<String,Object> appliedPaths;
	
	/**
	 * 处理目的URL[accessUrl]是否匹配该处理器
	 * @param request
	 * @param response
	 * @param accessUrl
	 * @return
	 */
	public boolean isMatchedFilter(HttpServletRequest request, HttpServletResponse response, String accessUrl) {
		if(!CollectionUtils.isEmpty(appliedPaths)) {
			for (String pattern : appliedPaths.keySet()) {
				if (pathsMatch(pattern, accessUrl)) {
					 return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 过滤目的URL[accessUrl]的访问
	 * @param request
	 * @param response
	 * @param accessUrl
	 * @return	true - 通过权限验证, false - 未通过权限验证
	 */
	public boolean filterAccess(HttpServletRequest request, HttpServletResponse response, String accessUrl) {
		try {
			if(!CollectionUtils.isEmpty(appliedPaths)) {
				for (String pattern : appliedPaths.keySet()) {
					if (pathsMatch(pattern, accessUrl)) {
						 Object pathConfig = appliedPaths.get(pattern);
						 if(!isAccessAllowed(request, response, pathConfig)) {
							 return false;
						 }
					}
				}
			}
			return true;
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			try {
				onAccessException(request, response, e);
			} catch (Throwable ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
			return false;
		}
	}
	
	/**
	 * 操作是否被允许
	 * @param request
	 * @param response
	 * @param pathConfig
	 * @return
	 * @throws Throwable
	 */
	protected abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object pathConfig) throws Throwable;
	
	/**
	 * 操作被拒绝
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public abstract void onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Throwable;
	
	/**
	 * 操作被允许
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public abstract void onAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws Throwable;
	
	/**
	 * 默认的发送异常时的处理
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public void onAccessException(HttpServletRequest request, HttpServletResponse response, Throwable e) throws Throwable {
		request.setAttribute(ShiroApplicationConstants.SHIRO_CLIENT_ACCESS_EXCEPTION, e);
		request.getRequestDispatcher("/shiro/accessible/exception").forward(request, response); // forward to SpringMVC Controller
	}

	protected boolean pathsMatch(String pattern, String path) {
        return pathMatcher.matches(pattern, path);
    }
	
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public PatternMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PatternMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public Map<String, Object> getAppliedPaths() {
		return appliedPaths;
	}

	public void setAppliedPaths(Map<String, Object> appliedPaths) {
		this.appliedPaths = appliedPaths;
	}
	
}
