package com.penglecode.xmodule.common.web.shiro.realm;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.penglecode.xmodule.common.web.shiro.service.ShiroPrincipalService;
/**
 * 抽象的当事人Realm
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月30日 上午9:38:46
 * @version  	1.0
 */
public abstract class AbstractPrincipalRealm<U,RO,RE> extends AuthorizingRealm {

	private ShiroPrincipalService<U,RO,RE> principalService;
	
	public ShiroPrincipalService<U,RO,RE> getPrincipalService() {
		return principalService;
	}

	public void setPrincipalService(ShiroPrincipalService<U,RO,RE> principalService) {
		this.principalService = principalService;
	}

    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
	
    public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
    	return super.getAuthorizationInfo(principals);
    }
    
}
