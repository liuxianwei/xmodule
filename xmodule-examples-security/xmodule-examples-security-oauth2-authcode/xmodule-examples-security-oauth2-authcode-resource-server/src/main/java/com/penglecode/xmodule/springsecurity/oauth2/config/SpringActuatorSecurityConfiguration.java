package com.penglecode.xmodule.springsecurity.oauth2.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * spring actuator安全配置
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
public class SpringActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final SecurityProperties securityProperties;
	
	public SpringActuatorSecurityConfiguration(SecurityProperties securityProperties) {
		super();
		this.securityProperties = securityProperties;
	}

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User defaultUser = securityProperties.getUser();
		auth.inMemoryAuthentication().passwordEncoder(passwordEncoder).withUser(defaultUser.getName())
				.password(passwordEncoder.encode(defaultUser.getPassword()))
				.roles(StringUtils.toStringArray(defaultUser.getRoles()));
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/admin/actuator/**") //指定actuator访问安全管辖路径
			.authorizeRequests()
	        .requestMatchers(EndpointRequest.to("info")).permitAll()
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
	    .and()
	        .httpBasic()
	    .and()
	    	.csrf().disable();
    }
	
}
