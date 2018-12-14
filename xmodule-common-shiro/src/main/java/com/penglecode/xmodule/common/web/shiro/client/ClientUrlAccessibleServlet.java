package com.penglecode.xmodule.common.web.shiro.client;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.config.Ini;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.shiro.ShiroUtils;
import com.penglecode.xmodule.common.web.shiro.client.ShiroClientAccessUtils.ShiroClientAccessURL;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;
import com.penglecode.xmodule.common.web.shiro.filter.UpmsAppShiroFilterFactoryBean;
import com.penglecode.xmodule.common.web.shiro.service.ShiroPermissionService;
import com.penglecode.xmodule.common.web.shiro.support.ShiroPermissionConfig;
import com.penglecode.xmodule.common.web.shiro.support.ShiroPermsSynchronizeEvent;

/**
 * 通过请求后台(shiro)来验证客户端指定的URL的访问权限
 * 
 * 例如：http://ip:port/flexedgex-upms/shiro/accessible
 * 
 * @author 	pengpeng
 * @date	2018年6月5日 下午4:35:33
 */
public class ClientUrlAccessibleServlet extends HttpServlet implements ApplicationListener<ShiroPermsSynchronizeEvent>, InitializingBean {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientUrlAccessibleServlet.class);
	
	private String dynamicPermissionsPlaceHolder = UpmsAppShiroFilterFactoryBean.DEFAULT_DYNAMIC_PERMISSIONS_PLACEHOLDER;
	
	private String filterChainDefinitions;
	
	private Map<String, String> filterChainDefinitionMap;
	
	private Map<String,ClientUrlAccessFilter> clientUrlAccessFilters = new LinkedHashMap<String,ClientUrlAccessFilter>();
	
	private volatile ClientUrlAccessFilterManager clientUrlAccessFilterManager;
	
	private ShiroPermissionService shiroPermissionService;
	
	public ClientUrlAccessibleServlet() {
		super();
		applyDefaultClientUrlAccessFilters();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ShiroClientAccessURL accessUrl = (ShiroClientAccessURL)request.getAttribute(ShiroApplicationConstants.SHIRO_CLIENT_ACCESS_URL);
			preHandleAccess(request, response, accessUrl);
			AccessResult accessResult = postHandleAccess(request, response, accessUrl);
			afterHandleAccess(accessResult, request, response, accessUrl);
		} catch (Exception ex) {
			try {
				clientUrlAccessFilterManager.getDefaultClientUrlAccessFilter().onAccessException(request, response, ex);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 前置处理
	 * @param request
	 * @param response
	 * @param accessUrl
	 */
	protected void preHandleAccess(HttpServletRequest request, HttpServletResponse response, ShiroClientAccessURL accessUrl) {
		Session session = ShiroUtils.getSession(); //create or touch session
		LOGGER.info(">>> Shiro accessUrl = {}, sid = {}", accessUrl, session.getId());
	}

	/**
	 * 处理客户端指定的URL的访问权限
	 * @param request
	 * @param response
	 * @param accessUrl
	 * @return true - 通过权限验证,false - 未通过权限验证
	 */
	protected AccessResult postHandleAccess(HttpServletRequest request, HttpServletResponse response, ShiroClientAccessURL accessUrl) {
		ClientUrlAccessFilterManager clientUrlAccessFilterManager = this.clientUrlAccessFilterManager;
		List<ClientUrlAccessFilter> matchedUrlAccessHandlers = clientUrlAccessFilterManager.getMatchedClientUrlAccessFilters(request, response, accessUrl.getAccessUrl());
		boolean accessAllowed = true;
		ClientUrlAccessFilter accessDeniedFilter = null, accessAllowedFilter = null, finalAccessFilter = null;
		try {
			if(!CollectionUtils.isEmpty(matchedUrlAccessHandlers)) {
				for(ClientUrlAccessFilter urlAccessFilter : matchedUrlAccessHandlers) {
					accessAllowed = urlAccessFilter.filterAccess(request, response, accessUrl.getAccessUrl());
					if(!accessAllowed) {
						accessDeniedFilter = urlAccessFilter;
						break;
					} else {
						accessAllowedFilter = urlAccessFilter;
					}
				}
			}
			if(accessAllowed) {
				accessAllowedFilter = ObjectUtils.defaultIfNull(accessAllowedFilter, clientUrlAccessFilterManager.getDefaultClientUrlAccessFilter());
				finalAccessFilter = accessAllowedFilter;
				LOGGER.info(">>> Using {} to handling accessUrl : {}", finalAccessFilter.getClass().getSimpleName(), accessUrl);
				accessAllowedFilter.onAccessAllowed(request, response);
			} else {
				finalAccessFilter = accessDeniedFilter;
				LOGGER.info(">>> Using {} to handling accessUrl : {}", finalAccessFilter.getClass().getSimpleName(), accessUrl);
				accessDeniedFilter.onAccessDenied(request, response);
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			if(finalAccessFilter != null) {
				try {
					finalAccessFilter.onAccessException(request, response, e);
				} catch (Throwable ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		}
		return new AccessResult(accessAllowed, accessDeniedFilter);
	}
	
	/**
	 * 后置处理
	 * @param accessResult
	 * @param request
	 * @param response
	 * @param accessUrl
	 */
	protected void afterHandleAccess(AccessResult accessResult, HttpServletRequest request, HttpServletResponse response, ShiroClientAccessURL accessUrl) {
		//如果是用户未登录而被拦截则保存请求URL，以便登录成功后重定向到该保存的URL
		if (!accessResult.isAccessAllowed()
				&& (accessResult.getAccessDeniedFilter() instanceof UserClientUrlAccessFilter
						|| accessResult.getAccessDeniedFilter() instanceof AuthcClientUrlAccessFilter)) {
			saveRequest(request, response, accessUrl.getAccessUrl());
		}
	}
	
	/**
	 * 初始化FilterChainDefinitionMap
	 */
	protected void initFilterChainDefinitionMap() {
		String dynamicPermissions = loadDynamicPermissions();
		String filterChainDefinitions = this.filterChainDefinitions.replace(this.dynamicPermissionsPlaceHolder, dynamicPermissions);
		filterChainDefinitions = prettyPermissionExpression(filterChainDefinitions);
		LOGGER.info("【{}】>>> Url based permission configuration :\n{}", getClass().getSimpleName(), filterChainDefinitions);
		Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
	}
	
	/**
	 * 动态加载permissions
	 * @return 例如：/admin/user/add=perms[user:add]
	 * 				  /admin/user/edit=perms[user:edit]
	 */
	protected String loadDynamicPermissions() {
		StringBuilder sb = new StringBuilder();
		List<ShiroPermissionConfig> appUrlPermissions = shiroPermissionService.getAppPermissionConfig(null);
		if (!CollectionUtils.isEmpty(appUrlPermissions)) {
			for (ShiroPermissionConfig permissionConfig : appUrlPermissions) {
				String expression = permissionConfig.getAppWebContextPath() + permissionConfig.getUrlPattern() + " = " + permissionConfig.getPermission();
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
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//reloadShiroPermissions(); //注掉,此处不需要主动调用,通过UpmsAppRedisShiroPermissionSynchronizer.afterPropertiesSet()触发ShiroPermsSynchronizeEvent进而调用onApplicationEvent方法
	}
	
	@Override
	public void onApplicationEvent(ShiroPermsSynchronizeEvent event) {
		reloadShiroPermissions();
	}
	
	/**
	 * 重新加载权限配置
	 */
	protected void reloadShiroPermissions() {
		initFilterChainDefinitionMap();
		initClientUrlAccessFilterManager();
	}
	
	/**
	 * 应用默认的ClientUrlAccessFilter配置
	 */
	protected void applyDefaultClientUrlAccessFilters() {
		clientUrlAccessFilters.put("anon", new AnonClientUrlAccessFilter());
		clientUrlAccessFilters.put("authc", new AuthcClientUrlAccessFilter());
		clientUrlAccessFilters.put("user", new UserClientUrlAccessFilter());
		clientUrlAccessFilters.put("perms", new PermsClientUrlAccessFilter());
	}
	
	/**
	 * 初始化clientUrlAccessFilterManager
	 */
	protected void initClientUrlAccessFilterManager() {
		ClientUrlAccessFilterManager clientUrlAccessFilterManager = createClientUrlAccessFilterManager();
		clientUrlAccessFilterManager.applyFilterChains(getFilterChainDefinitionMap());
		setClientUrlAccessFilterManager(clientUrlAccessFilterManager);
	}
	
	/**
	 * 创建ClientUrlAccessFilterManager
	 * @return
	 */
	protected ClientUrlAccessFilterManager createClientUrlAccessFilterManager() {
		return new ClientUrlAccessFilterManager(clientUrlAccessFilters);
	}
	
	/**
	 * 保存请求url
	 * @param request
	 * @param response
	 * @param accessUrl
	 */
	protected void saveRequest(HttpServletRequest request, HttpServletResponse response, String accessUrl) {
		String backUrl = request.getHeader("referer"); //一般都是被拦截的静态html页面,例如：http://127.0.0.1/flexedgex-upms-web/index.html
		if(!StringUtils.isEmpty(backUrl) && backUrl.contains(accessUrl)) {
	        Session session = ShiroUtils.getSession();
	        ClientSavedRequest savedRequest = new ClientSavedRequest(request, backUrl);
	        session.setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
	        LOGGER.info(">>> Saving request url : " + savedRequest.getRequestUrl());
		}
	}
	
	protected String getDynamicPermissionsPlaceHolder() {
		return dynamicPermissionsPlaceHolder;
	}

	public void setDynamicPermissionsPlaceHolder(String dynamicPermissionsPlaceHolder) {
		this.dynamicPermissionsPlaceHolder = dynamicPermissionsPlaceHolder;
	}

	protected String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	protected Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	protected Map<String, ClientUrlAccessFilter> getClientUrlAccessFilters() {
		return clientUrlAccessFilters;
	}

	public void setClientUrlAccessFilters(Map<String, ClientUrlAccessFilter> clientUrlAccessFilters) {
		if(!CollectionUtils.isEmpty(clientUrlAccessFilters)) {
			this.clientUrlAccessFilters.putAll(clientUrlAccessFilters);
		}
	}

	protected ShiroPermissionService getShiroPermissionService() {
		return shiroPermissionService;
	}

	public void setShiroPermissionService(ShiroPermissionService shiroPermissionService) {
		this.shiroPermissionService = shiroPermissionService;
	}

	protected ClientUrlAccessFilterManager getClientUrlAccessFilterManager() {
		return clientUrlAccessFilterManager;
	}

	protected void setClientUrlAccessFilterManager(ClientUrlAccessFilterManager clientUrlAccessFilterManager) {
		this.clientUrlAccessFilterManager = clientUrlAccessFilterManager;
	}
	
	public static class AccessResult {
		
		private final boolean accessAllowed;
		
		private final ClientUrlAccessFilter accessDeniedFilter;

		public AccessResult(boolean accessAllowed, ClientUrlAccessFilter accessDeniedFilter) {
			super();
			this.accessAllowed = accessAllowed;
			this.accessDeniedFilter = accessDeniedFilter;
		}

		public boolean isAccessAllowed() {
			return accessAllowed;
		}

		public ClientUrlAccessFilter getAccessDeniedFilter() {
			return accessDeniedFilter;
		}
		
	}

}