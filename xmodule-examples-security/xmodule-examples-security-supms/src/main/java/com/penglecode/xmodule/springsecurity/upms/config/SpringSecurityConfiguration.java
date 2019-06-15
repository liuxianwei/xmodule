package com.penglecode.xmodule.springsecurity.upms.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;

import com.penglecode.xmodule.common.boot.config.FilterRegistrationOrder;
import com.penglecode.xmodule.common.security.config.SecurityConfigProperties;
import com.penglecode.xmodule.springsecurity.upms.web.security.authc.CustomAuthenticationEntryPoint;
import com.penglecode.xmodule.springsecurity.upms.web.security.authc.CustomAuthenticationFailureHandler;
import com.penglecode.xmodule.springsecurity.upms.web.security.authc.CustomAuthenticationSuccessHandler;
import com.penglecode.xmodule.springsecurity.upms.web.security.authc.DefaultUserDetailsService;
import com.penglecode.xmodule.springsecurity.upms.web.security.authz.CustomAccessDecisionManager;
import com.penglecode.xmodule.springsecurity.upms.web.security.authz.CustomAccessDeniedHandler;
import com.penglecode.xmodule.springsecurity.upms.web.security.authz.CustomFilterInvocationSecurityMetadataSource;
import com.penglecode.xmodule.springsecurity.upms.web.security.filter.CustomFilterSecurityInterceptor;
import com.penglecode.xmodule.springsecurity.upms.web.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.penglecode.xmodule.springsecurity.upms.web.security.filter.RestoreSecurityAccessDecideRequestFilter;
import com.penglecode.xmodule.springsecurity.upms.web.security.servlet.SecurityAccessDecideRequestFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityConfigProperties securityConfigProperties;
	
	@Bean(name="securityAccessDecideRequestFilter")
	public SecurityAccessDecideRequestFilter securityAccessDecideRequestFilter() {
		return new SecurityAccessDecideRequestFilter();
	}
	
	@Bean
	public FilterRegistrationBean<Filter> enableSecurityAccessDecideRequestFilter(@Qualifier("securityAccessDecideRequestFilter") Filter filter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(true);
	    filterRegistrationBean.setName("securityAccessDecideRequestFilter");
	    filterRegistrationBean.addUrlPatterns("/*");
	    filterRegistrationBean.setOrder(FilterRegistrationOrder.ORDER_SECURITY_ACCESS_DECIDE_REQUEST_FILTER);
	    return filterRegistrationBean;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/error").permitAll()
				.antMatchers("/security/accessdecide").permitAll()
				.antMatchers("/security/unauthenticated", "/security/unauthorized").permitAll()
				.anyRequest().authenticated() //所有其他路径必须经过身份验证。
				.and()
			.formLogin()
				.loginPage("/login").permitAll() //由loginPage()指定的自定义“登录”页面，任何人都可以查看它。
				.and()
			.logout()
				.logoutSuccessHandler(new ForwardLogoutSuccessHandler("/logout/success"))
				.logoutUrl("/logout").permitAll() //由logoutUrl()指定的自定义“退出”页面，任何人都可以查看它。
			.and()
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint()).accessDeniedHandler(customAccessDeniedHandler());
		http.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //添加自定义的用户名密码认证过滤器
		http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class); //添加自定义过滤器实现动态权限验证
		http.addFilterAfter(restoreSecurityAccessDecideRequestFilter(), FilterSecurityInterceptor.class);
	}

	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint("/security/unauthenticated", "/security/accessdecide/unauthenticated"); //跳转到controller方法中处理
	}
	
	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler("/security/unauthorized", "/security/accessdecide/unauthorized"); //跳转到controller方法中处理
	}
	
	/**
	 * 通过显式声明DaoAuthenticationProvider为容器bean使得MessageSourceAware.setMessageSource起作用
	 * 从而解决其中默认硬编码写死的SpringSecurityMessageSource的问题
	 */
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

	/**
	 * 暴露AuthenticationManager bean
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 暴露UserDetailsService bean
	 */
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return new DefaultUserDetailsService();
	}
	
	/**
	 * 动态URL-ROLE授权之自定义的FilterSecurityInterceptor
	 */
	@Bean(name="customFilterSecurityInterceptor")
	public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
		CustomFilterSecurityInterceptor filterSecurityInterceptor = new CustomFilterSecurityInterceptor();
		filterSecurityInterceptor.setAccessDecisionManager(customAccessDecisionManager());
		filterSecurityInterceptor.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource());
		filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
		return filterSecurityInterceptor;
	}
	
	/**
	 * 取消自动注册customFilterSecurityInterceptor
	 */
	@Bean
	public FilterRegistrationBean<Filter> disableFilterSecurityInterceptor(@Qualifier("customFilterSecurityInterceptor") Filter filter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false); //取消自动注册
	    return filterRegistrationBean;
	}
	
	/**
	 * 自定义的UsernamePasswordAuthenticationFilter
	 */
	@Bean(name="customUsernamePasswordAuthenticationFilter")
	public UsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
		CustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
		usernamePasswordAuthenticationFilter.setUsernameParameter("userName");
		usernamePasswordAuthenticationFilter.setPasswordParameter("password");
		usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler("/login/failure"));
		usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler("/login/success", securityConfigProperties.getDefaultLoginSuccessUrl(), false, "backurl"));
		usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		return usernamePasswordAuthenticationFilter;
	}
	
	/**
	 * 取消自动注册customUsernamePasswordAuthenticationFilter
	 */
	@Bean
	public FilterRegistrationBean<Filter> disableUsernamePasswordAuthenticationFilter(@Qualifier("customUsernamePasswordAuthenticationFilter") Filter filter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false); //取消自动注册
	    return filterRegistrationBean;
	}
	
	@Bean(name="restoreSecurityAccessDecideRequestFilter")
	public RestoreSecurityAccessDecideRequestFilter restoreSecurityAccessDecideRequestFilter() {
		return new RestoreSecurityAccessDecideRequestFilter();
	}
	
	/**
	 * 取消自动注册restoreSecurityAccessDecideRequestFilter
	 */
	@Bean
	public FilterRegistrationBean<Filter> disableRestoreSecurityAccessDecideRequestFilter(@Qualifier("restoreSecurityAccessDecideRequestFilter") Filter filter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false); //取消自动注册
	    return filterRegistrationBean;
	}
	
	/**
	 * 动态URL-ROLE授权之自定义的AccessDecisionManager
	 */
	@Bean
	public AccessDecisionManager customAccessDecisionManager() {
		return new CustomAccessDecisionManager();
	}
	
	/**
	 * 动态URL-ROLE授权之自定义的FilterInvocationSecurityMetadataSource,用于从数据库中动态加载URL-ROLES的配置关系
	 */
	@Bean
	public FilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource() {
		return new CustomFilterInvocationSecurityMetadataSource();
	}

}
