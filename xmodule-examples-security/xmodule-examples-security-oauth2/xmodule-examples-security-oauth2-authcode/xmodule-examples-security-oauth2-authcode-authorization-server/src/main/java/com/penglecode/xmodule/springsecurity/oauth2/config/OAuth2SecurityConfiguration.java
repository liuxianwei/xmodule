package com.penglecode.xmodule.springsecurity.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.penglecode.xmodule.springsecurity.oauth2.service.UpmsUserDetailsService;

/**
 * 登录认证安全配置
 * 
 * @author 	pengpeng
 * @date	2019年2月13日 下午3:00:26
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		/**
		 * 纳入匹配的路径("/oauth/**", "/login", "/logout")，将使用自带内置的用户名/密码登录页面(DefaultLoginPageGeneratingFilter)
		 */
		http.requestMatchers().antMatchers("/oauth/**", "/login", "/logout")
		.and()
			.authorizeRequests()
			.antMatchers("/oauth/check_token").permitAll() //token检测默认使用的basic认证，因此就不需要用户名/密码身份验证
			.anyRequest().authenticated() //所有其他路径必须经过用户名/密码身份验证
	    .and()
	        .formLogin()
	    .and()
	    	.csrf().disable();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return new UpmsUserDetailsService();
	}
	
}
