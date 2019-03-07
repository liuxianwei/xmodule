package com.penglecode.xmodule.springsecurity.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.web.security.oauth2.CustomOAuth2ExceptionRenderer;
import com.penglecode.xmodule.common.web.security.oauth2.OAuth2Client;
import com.penglecode.xmodule.common.web.security.oauth2.OAuth2ServerConfigProperties;

/**
 * SpringSecurity-OAuth2认证授权服务器配置
 * 
 * @author 	pengpeng
 * @date	2019年2月13日 上午10:17:15
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private OAuth2ServerConfigProperties oauth2ServerConfig;
	
	/**
	 * OAuth2认证授权异常渲染
	 * @return
	 */
	@Bean(name="oauth2ExceptionRenderer")
	public CustomOAuth2ExceptionRenderer oauth2ExceptionRenderer() {
		CustomOAuth2ExceptionRenderer oauth2ExceptionRenderer = new CustomOAuth2ExceptionRenderer();
		oauth2ExceptionRenderer.setResponseContentType(MediaType.APPLICATION_JSON_UTF8);
		return oauth2ExceptionRenderer;
	}
	
	/**
	 * 禁止访问处理
	 * @return
	 */
	@Bean(name="oauth2AccessDeniedHandler")
	public OAuth2AccessDeniedHandler oauth2AccessDeniedHandler() {
		OAuth2AccessDeniedHandler oauth2AccessDeniedHandler = new OAuth2AccessDeniedHandler();
		oauth2AccessDeniedHandler.setExceptionRenderer(oauth2ExceptionRenderer());
		return oauth2AccessDeniedHandler;
	}
	
	/**
	 * AuthorizationServerSecurityConfigurer：用来配置令牌端点(Token Endpoint)的安全约束.(pre认证，一般是httpbasic)
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.addObjectPostProcessor(new ObjectPostProcessor<ClientCredentialsTokenEndpointFilter>() {
			@Override
			public <O extends ClientCredentialsTokenEndpointFilter> O postProcess(O object) {
				OAuth2AuthenticationEntryPoint authenticationEntryPoint = ReflectionUtils.getFieldValue(object, "authenticationEntryPoint");
				authenticationEntryPoint.setExceptionRenderer(oauth2ExceptionRenderer());
				return object;
			}
		});
	    security.checkTokenAccess("isAuthenticated()") //允许在basic认证通过的情况下检测token。默认denyAll()，在ResourceServer启用RemoteTokenServices来检测token时需要放开
	    		.tokenKeyAccess("permitAll()")
	    		.allowFormAuthenticationForClients() //允许表单登录
	    		.accessDeniedHandler(oauth2AccessDeniedHandler());
	}

	protected ClientCredentialsTokenEndpointFilter process(ClientCredentialsTokenEndpointFilter object) {
		return object;
	}
	
	/**
	 * ClientDetailsServiceConfigurer：用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
	 * 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		InMemoryClientDetailsServiceBuilder build = clients.inMemory();
		if(!ArrayUtils.isEmpty(oauth2ServerConfig.getClients())) {
			for(OAuth2Client client : oauth2ServerConfig.getClients()) {
				build.withClient(client.getClientId())
					.secret(passwordEncoder.encode(client.getClientSecret()))
                	.accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
	                .refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds())
	                .authorizedGrantTypes(client.getAuthorizedGrantTypes()) //使用配置文件中设置的password模式
	                .resourceIds(client.getResourceIds())
	                .scopes(client.getScope());
			}
		}
	}

	/**
	 * AuthorizationServerEndpointsConfigurer：用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。(after认证，即用户名+密码认证)
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore);
	}
	
}
