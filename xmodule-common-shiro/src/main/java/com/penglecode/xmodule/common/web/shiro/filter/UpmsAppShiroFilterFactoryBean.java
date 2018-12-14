package com.penglecode.xmodule.common.web.shiro.filter;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.Filter;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.web.shiro.service.ShiroPermissionService;
import com.penglecode.xmodule.common.web.shiro.support.ShiroPermissionConfig;
import com.penglecode.xmodule.common.web.shiro.support.ShiroPermsSynchronizeEvent;

/**
 * 可重新加载filterChainDefinitions配置的ShiroFilterFactoryBean
 * 通过Spring事件机制来触发Shiro权限的重新加载
 * 
 * @author 	pengpeng
 * @date	2018年6月2日 下午4:54:52
 */
public class UpmsAppShiroFilterFactoryBean extends ShiroFilterFactoryBean implements ApplicationContextAware, ApplicationListener<ShiroPermsSynchronizeEvent>, InitializingBean {

	public static final String DEFAULT_DYNAMIC_PERMISSIONS_PLACEHOLDER = "${dynamicPermissions}";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsAppShiroFilterFactoryBean.class);
	
	private ApplicationContext applicationContext;
	
	private String dynamicPermissionsPlaceHolder = DEFAULT_DYNAMIC_PERMISSIONS_PLACEHOLDER;
	
	private ShiroPermissionService shiroPermissionService;
	
	private String filterChainDefinitions;
	
	/** 即创建的ShiroFilter作用于哪个应用的URL,如果为null则作用于全部URL */
	private Long appId;
	
	/** 创建的ShiroFilter的名字 */
	private String shiroFilterName;
	
	/** 未认证URL */
	private String unauthenticatedUrl;
	
	/**
	 * 去掉自动扫描容器中的Filter并加入到ShiroFilterFactoryBean.filters中
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	/**
	 * 初始化FilterChainDefinitionMap
	 */
	protected void initFilterChainDefinitionMap() {
		String dynamicPermissions = loadDynamicPermissions();
		String filterChainDefinitions = this.filterChainDefinitions.replace(this.dynamicPermissionsPlaceHolder, dynamicPermissions);
		filterChainDefinitions = prettyPermissionExpression(filterChainDefinitions);
		LOGGER.info("【{}】>>> Url based permission configuration :\n{}", shiroFilterName, filterChainDefinitions);
		super.setFilterChainDefinitions(filterChainDefinitions);
	}
	
	/**
	 * 动态加载permissions
	 * @return 例如：/admin/user/add=perms[user:add]
	 * 				  /admin/user/edit=perms[user:edit]
	 */
	protected String loadDynamicPermissions() {
		StringBuilder sb = new StringBuilder();
		List<ShiroPermissionConfig> appUrlPermissions = shiroPermissionService.getAppPermissionConfig(appId);
		if (!CollectionUtils.isEmpty(appUrlPermissions)) {
			for (ShiroPermissionConfig permissionConfig : appUrlPermissions) {
				String expression = permissionConfig.getUrlPattern() + " = " + permissionConfig.getPermission();
				sb.append(expression);
				if(!expression.endsWith("\n")){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	protected String prettyPermissionExpression(String expression){
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(expression);
		while(scanner.hasNextLine()){
			String line =  org.apache.shiro.util.StringUtils.clean(scanner.nextLine());
			if (line == null || line.startsWith(Ini.COMMENT_POUND) || line.startsWith(Ini.COMMENT_SEMICOLON)) {
                //skip empty lines and comments:
                continue;
            }
			if(!line.endsWith("\n")){
				line += "\n";
			}
			sb.append("\t\t" + line);
		}
		scanner.close();
		return sb.toString();
	}
	
	public void reloadShiroPermissions() {
		AbstractShiroFilter shiroFilter = applicationContext.getBean(shiroFilterName, AbstractShiroFilter.class);
		initFilterChainDefinitionMap(); //重新加载Shiro权限配置
		PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(createFilterChainManager());
		shiroFilter.setFilterChainResolver(chainResolver);
	}
	
	public String getDynamicPermissionsPlaceHolder() {
		return dynamicPermissionsPlaceHolder;
	}

	public void setDynamicPermissionsPlaceHolder(String dynamicPermissionsPlaceHolder) {
		this.dynamicPermissionsPlaceHolder = dynamicPermissionsPlaceHolder;
	}

	public ShiroPermissionService getShiroPermissionService() {
		return shiroPermissionService;
	}

	public void setShiroPermissionService(ShiroPermissionService shiroPermissionService) {
		this.shiroPermissionService = shiroPermissionService;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getShiroFilterName() {
		return shiroFilterName;
	}

	public void setShiroFilterName(String shiroFilterName) {
		this.shiroFilterName = shiroFilterName;
	}

	public String getUnauthenticatedUrl() {
		return unauthenticatedUrl;
	}

	public void setUnauthenticatedUrl(String unauthenticatedUrl) {
		this.unauthenticatedUrl = unauthenticatedUrl;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void onApplicationEvent(ShiroPermsSynchronizeEvent event) {
		reloadShiroPermissions(); //重新加载shiro权限配置
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//reloadShiroPermissions(); //注掉,此处不需要主动调用,通过UpmsAppRedisShiroPermissionSynchronizer.afterPropertiesSet()触发ShiroPermsSynchronizeEvent进而调用onApplicationEvent方法
		Map<String,Filter> filters = getFilters();
		for(Map.Entry<String,Filter> entry : filters.entrySet()) {
			Filter filter = entry.getValue();
			applyUnauthenticatedUrlIfNecessary(filter);
		}
	}
	
	protected void applyUnauthenticatedUrlIfNecessary(Filter filter) {
        String unauthenticatedUrl = getUnauthenticatedUrl();
        if (StringUtils.hasText(unauthenticatedUrl) && (filter instanceof UnauthenticatedUrlAware)) {
        	UnauthenticatedUrlAware unauthenticatedUrlAware = (UnauthenticatedUrlAware) filter;
            //only apply the unauthenticatedUrl if they haven't explicitly configured one already:
            String existingUnauthenticatedUrl = unauthenticatedUrlAware.getUnauthenticatedUrl();
            if (existingUnauthenticatedUrl == null) {
            	unauthenticatedUrlAware.setUnauthenticatedUrl(unauthenticatedUrl);
            }
        }
    }
	
}
