package com.penglecode.xmodule.common.web.shiro.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.util.Assert;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;
import com.penglecode.xmodule.upms.model.UpmsApp;

public class ShiroClientAccessUtils {
	
	/**
	 * 创建ShiroClientAccessURL
	 * @param accessUrl
	 * @return
	 */
	public static ShiroClientAccessURL createClientAccessURL(String accessUrl) {
		return new ShiroClientAccessURL(accessUrl);
	}
	
	/**
	 * 根据appWebContextPath获取应用信息
	 * @param appWebContextPath
	 * @return
	 */
	public static UpmsApp getClientAccessAppInfo(String appWebContextPath) {
		UpmsApp app = ShiroApplicationConstants.ALL_UPMS_APP_MAPPING.get(appWebContextPath);
		Assert.notNull(app, String.format("No UpmsApp found with appWebContextPath : %s", appWebContextPath));
		return app;
	}
	
	/**
	 * 获取应用路径[contextPath + requestUri]
	 * @param accessUrl
	 * @return
	 */
	public static String getPathWithinApplication(String accessUrl) {
		Assert.hasText(accessUrl, "Parameter 'accessUrl' can not be empty!");
		try {
			URL url = new URL(accessUrl);
			return url.getPath();
		} catch (MalformedURLException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	/**
	 * 从accessUrl中提取[contextPath, requestUri]
	 * @param accessUrl
	 * @return
	 */
	public static String[] getRequestPaths(String accessUrl) {
		Assert.hasText(accessUrl, "Parameter 'accessUrl' can not be empty!");
		if(accessUrl.toLowerCase().startsWith("http")) {
			accessUrl = getPathWithinApplication(accessUrl);
		}
		String[] paths = new String[2];
		String path = accessUrl;
		int index = path.indexOf('/', 1);
		if(index == -1) {
			paths[0] = path;
			paths[1] = "/";
		} else {
			paths[0] = path.substring(0, index);
			paths[1] = path.substring(index);
		}
		return paths;
	}
	
	public static class ShiroClientAccessURL {
		
		private final String accessUrl;
		
		private final String requestUri;
		
		private final String appWebContextPath;

		protected ShiroClientAccessURL(String accessUrl) {
			super();
			this.accessUrl = ShiroClientAccessUtils.getPathWithinApplication(accessUrl);
			String[] paths = ShiroClientAccessUtils.getRequestPaths(this.accessUrl);
			this.appWebContextPath = paths[0];
			this.requestUri = paths[1];
		}

		public String getAccessUrl() {
			return accessUrl;
		}

		public String getRequestUri() {
			return requestUri;
		}

		public String getAppWebContextPath() {
			return appWebContextPath;
		}
		
		@Override
		public String toString() {
			return accessUrl;
		}
		
	}
	
}
