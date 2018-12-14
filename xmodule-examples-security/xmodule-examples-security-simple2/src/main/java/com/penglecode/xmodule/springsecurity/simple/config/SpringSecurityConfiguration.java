package com.penglecode.xmodule.springsecurity.simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.penglecode.xmodule.springsecurity.simple.web.security.CustomAuthenticationFailureHandler;
import com.penglecode.xmodule.springsecurity.simple.web.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	  return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/home").permitAll() //“/”和“/ home”路径被配置为不需要任何身份验证
				.anyRequest().authenticated() //所有其他路径必须经过身份验证。
				.and()
			.formLogin()
				.failureHandler(new CustomAuthenticationFailureHandler("/login"))
				.successHandler(new CustomAuthenticationSuccessHandler("/hello", false, "backurl"))
				.loginPage("/login").permitAll() //由loginPage()指定的自定义“登录”页面，任何人都可以查看它。
				.and()
			.logout()
				.logoutUrl("/logout").permitAll() //由logoutUrl()指定的自定义“退出”页面，任何人都可以查看它。
			.and()
				.csrf().disable();
				
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("admin").password("123456").roles("ADMIN");
	}
	
}
