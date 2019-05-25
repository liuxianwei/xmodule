package com.penglecode.xmodule.springcloud.busexample.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring-boot-starter-actuator启用安全认证
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */
@Configuration
@EnableWebSecurity
public class SpringActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
	        .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
	        .antMatchers("/**").permitAll()
	    .and()
	        .formLogin()
	    .and()
	    	.csrf().disable();
    }
	
}
