package com.penglecode.xmodule.common.web.security.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * 基于OAuth2认证的接口API辅助类
 * 
 * @author 	pengpeng
 * @date	2019年2月25日 下午3:49:07
 */
public abstract class OAuth2HttpApiResourceSupport extends HttpAPIResourceSupport {

	@Autowired
	private ResourceServerTokenServices tokenServices;
	
	/**
	 * 获取当前请求所带token指示的已认证Principal(用户信息)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAuthenticatedPrincipal() {
		HttpServletRequest request = SpringWebMvcUtils.getCurrentHttpServletRequest();
		String token = SpringSecurityOAuth2Utils.extractHeaderToken(request);
		Assert.hasText(token, "No token found in http request header!");
		OAuth2Authentication authentication = getTokenServices().loadAuthentication(token);
		return (T) authentication.getPrincipal();
	}

	protected ResourceServerTokenServices getTokenServices() {
		return tokenServices;
	}

	protected void setTokenServices(ResourceServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}
	
}
