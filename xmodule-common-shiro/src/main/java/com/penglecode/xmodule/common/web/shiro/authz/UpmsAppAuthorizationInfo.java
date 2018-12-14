package com.penglecode.xmodule.common.web.shiro.authz;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.web.shiro.ShiroUtils;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;

public class UpmsAppAuthorizationInfo<T> implements AuthorizationInfo {

	private static final long serialVersionUID = 1L;

	private Map<Long,Set<String>> appIdRolesMapping = new HashMap<Long,Set<String>>();
	
	private Map<Long,Set<String>> appIdStringPermissionsMapping = new HashMap<Long,Set<String>>();
	
	private Map<Long,Set<Permission>> appIdObjectPermissionsMapping = new HashMap<Long,Set<Permission>>();
	
	private Map<Long,Set<T>> appIdResourcesMapping = new HashMap<Long,Set<T>>();

	protected Map<Long, Set<String>> getAppIdRolesMapping() {
		return appIdRolesMapping;
	}

	public void setAppIdRolesMapping(Map<Long, Set<String>> appIdRolesMapping) {
		if(appIdRolesMapping == null) {
			appIdRolesMapping = new HashMap<Long,Set<String>>();
		}
		this.appIdRolesMapping = appIdRolesMapping;
	}

	protected Map<Long, Set<String>> getAppIdStringPermissionsMapping() {
		return appIdStringPermissionsMapping;
	}

	public void setAppIdStringPermissionsMapping(Map<Long, Set<String>> appIdStringPermissionsMapping) {
		if(appIdStringPermissionsMapping == null) {
			appIdStringPermissionsMapping = new HashMap<Long,Set<String>>();
		}
		this.appIdStringPermissionsMapping = appIdStringPermissionsMapping;
	}

	protected Map<Long, Set<Permission>> getAppIdObjectPermissionsMapping() {
		return appIdObjectPermissionsMapping;
	}

	public void setAppIdObjectPermissionsMapping(Map<Long, Set<Permission>> appIdObjectPermissionsMapping) {
		if(appIdObjectPermissionsMapping == null) {
			appIdObjectPermissionsMapping = new HashMap<Long,Set<Permission>>();
		}
		this.appIdObjectPermissionsMapping = appIdObjectPermissionsMapping;
	}

	protected Map<Long, Set<T>> getAppIdResourcesMapping() {
		return appIdResourcesMapping;
	}

	public void setAppIdResourcesMapping(Map<Long, Set<T>> appIdResourcesMapping) {
		if(appIdResourcesMapping == null) {
			appIdResourcesMapping = new HashMap<Long,Set<T>>();
		}
		this.appIdResourcesMapping = appIdResourcesMapping;
	}

	@Override
	public Collection<String> getRoles() {
		return appIdRolesMapping.get(getCurrentAppIdOfRequest());
	}

	@Override
	public Collection<String> getStringPermissions() {
		return appIdStringPermissionsMapping.get(getCurrentAppIdOfRequest());
	}

	@Override
	public Collection<Permission> getObjectPermissions() {
		return appIdObjectPermissionsMapping.get(getCurrentAppIdOfRequest());
	}
	
	public Collection<T> getResources() {
		return appIdResourcesMapping.get(getCurrentAppIdOfRequest());
	}
	
	protected Long getCurrentAppIdOfRequest() {
		HttpServletRequest request = ShiroUtils.getCurrentRequest();
		Long currentAppIdOfRequest = (Long) request.getAttribute(ShiroApplicationConstants.CURRENT_CLIENT_ACCESS_APPLICATION_ID);
		Long appId = ObjectUtils.defaultIfNull(currentAppIdOfRequest, ApplicationConstants.CURRENT_APPLICATION_ID);
		//System.out.println(">>> requestUrl = " + request.getHeader("referer") + " appId = " + appId);
		return appId;
	}
	
}
