package com.penglecode.xmodule.springsecurity.upms.web.security.authz;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.penglecode.xmodule.common.support.MvvmWebAppConfig;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.security.service.RoleResourceService;
import com.penglecode.xmodule.common.web.security.support.RoleResource;
import com.penglecode.xmodule.springsecurity.upms.web.security.support.SecurityPermsSynchronizeEvent;

public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, ApplicationListener<SecurityPermsSynchronizeEvent>, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);
	
	private final Map<RequestMatcher, Set<ConfigAttribute>> securityPermissions = new HashMap<RequestMatcher, Set<ConfigAttribute>>();
	
	/**
	 * 如果资源没有配置任何角色-资源关系时，则保险期间该资源应该赋予没人可以访问的权限
	 */
	private String supremeRole = "SUPERME";
	
	@Autowired
	private MvvmWebAppConfig mvvmWebAppConfigProperties;
	
	@Autowired
	private RoleResourceService roleResourceService;
	
	protected String getSupremeRole() {
		return supremeRole;
	}

	public void setSupremeRole(String supremeRole) {
		this.supremeRole = supremeRole;
	}

	protected RoleResourceService getRoleResourceService() {
		return roleResourceService;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		 
        Collection<ConfigAttribute> configAttrs = null;
        for (Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
            if (entry.getKey().matches(request)) {
            	configAttrs = entry.getValue();
                break;
            }
        }
        return configAttrs;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public void onApplicationEvent(SecurityPermsSynchronizeEvent event) {
		reloadSecurityPermissions();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		reloadSecurityPermissions();
	}

	protected Map<RequestMatcher, Set<ConfigAttribute>> getSecurityPermissions() {
		return securityPermissions;
	}
	
	protected synchronized void reloadSecurityPermissions() {
		LOGGER.info(">>> 重新加载所有资源-角色配置关系!");
		securityPermissions.clear();
		List<RoleResource> allRoleResourceMappings = roleResourceService.getAllRoleResourceMappings();
		if(!CollectionUtils.isEmpty(allRoleResourceMappings)) {
			for(RoleResource roleResource : allRoleResourceMappings) {
				RequestMatcher requestMatcher = getRequestMatcher(roleResource.getResourceUrl(), roleResource.getHttpMethod());
				Set<ConfigAttribute> configAttrs = securityPermissions.get(requestMatcher);
				if(configAttrs == null) {
					configAttrs = new LinkedHashSet<ConfigAttribute>();
					getSecurityPermissions().put(requestMatcher, configAttrs);
				}
				configAttrs.add(new SecurityConfig(StringUtils.defaultIfEmpty(roleResource.getRoleCode(), supremeRole)));
			}
		}
		for(Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
			LOGGER.info(">>> {} : {}", entry.getKey(), entry.getValue());
		}
	}
	
	protected RequestMatcher getRequestMatcher(String url, String httpMethod) {
		url = mvvmWebAppConfigProperties.getAppWebContextPath() + url;
		if(httpMethod == null || HttpMethod.resolve(httpMethod) == null) {
			httpMethod = null;
		}
        return new AntPathRequestMatcher(url, httpMethod);
    }

}
