package com.penglecode.xmodule.springsecurity.upms.web.security.authz;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.penglecode.xmodule.common.util.CollectionUtils;

public class CustomAccessDecisionManager implements AccessDecisionManager {

	/* 
     * 该方法决定该权限是否有权限访问该资源，其实object就是一个资源的地址，authentication是当前用户的
     * 对应权限，如果没登陆就为游客，登陆了就是该用户对应的权限
     */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if(!CollectionUtils.isEmpty(configAttributes)) {
			for(ConfigAttribute configAttribute : configAttributes) {
				//访问所请求资源所需要的权限(角色)
	            String accessNeedRole = configAttribute.getAttribute();
	            //当前用户所拥有的权限(角色)
	            Collection<? extends GrantedAuthority> currentUserAuthorities = authentication.getAuthorities();
	            for(GrantedAuthority authority : currentUserAuthorities) {
	            	if(accessNeedRole.equals(authority.getAuthority())) {
	            		return;
	            	}
	            }
			}
	        throw new AccessDeniedException("Access Denied!");
        }
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
