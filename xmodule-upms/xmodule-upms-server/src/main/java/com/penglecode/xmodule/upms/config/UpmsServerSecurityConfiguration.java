package com.penglecode.xmodule.upms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * UPMS Server安全配置
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午4:02:37
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class UpmsServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
	        .antMatchers("/**").permitAll()
	    .and()
	        .formLogin()
	    .and()
	    	.csrf().disable();
    }
	
}