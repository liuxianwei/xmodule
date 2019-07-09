package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.common.support.AbstractXTreeBuilder;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceActionTypeEnum;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.service.UpmsUserService;
import com.penglecode.xmodule.upms.support.UpmsResourceNavMenuNodeConverter;
import com.penglecode.xmodule.upms.support.UpmsResourceTreeBuilder;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * 当前登录用户的相关接口
 * 
 * @author 	pengpeng
 * @date	2019年1月22日 下午2:38:56
 */
@RestController
@RequestMapping("/login/user")
public class UpmsLoginController extends HttpAPIResourceSupport {

	private AbstractXTreeBuilder<Long, UpmsResource> resourceTreeBuilder = new UpmsResourceTreeBuilder();
	
	private TreeNodeConverter<UpmsResource,Map<String,Object>> resourceNavMenuNodeConverter = new UpmsResourceNavMenuNodeConverter();
	
	@Autowired
	private UpmsUserService upmsUserService;
	
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	@GetMapping(value="/current", produces=APPLICATION_JSON)
	public Result<Map<String,Object>> getLoginUserInfo() {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		Map<String,Object> loginUserInfo = getLoginUserInfoMap(loginUser);
		return Result.success().message("OK").data(loginUserInfo).build();
	}
	
	/**
	 * 后台管理公共页面所有接口数据整合ALL-IN-ONE
	 * @return
	 */
	@GetMapping(value="/allinfo", produces=APPLICATION_JSON)
	public Result<Map<String,Object>> getLoginUserAllInfo() {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		Map<String,Object> allInfo = new HashMap<String,Object>();
		allInfo.put("loginUserInfo", getLoginUserInfoMap(loginUser));
		allInfo.put("loginUserAsideMenus", getLoginUserAsideMenuList(loginUser.getUserId()));
		allInfo.put("loginUserTopnavMenus", getLoginUserTopnavMenuList(loginUser.getUserId()));
		return Result.success().message("OK").data(allInfo).build();
	}
	
	/**
	 * 获取当前登录用户信息的Map形式
	 * @param loginUser
	 * @return
	 */
	protected Map<String,Object> getLoginUserInfoMap(UpmsLoginUser loginUser) {
		Map<String,Object> user = new HashMap<String,Object>();
		user.put("userId", loginUser.getUserId());
		user.put("userName", loginUser.getUserName());
		user.put("realName", loginUser.getRealName());
		user.put("userIconUrl", loginUser.getUserIconUrl());
		user.put("email", loginUser.getEmail());
		user.put("mobilePhone", loginUser.getMobilePhone());
		user.put("qq", null);
		user.put("lastLoginTime", loginUser.getLastLoginTime());
		user.put("messages", new ArrayList<Object>()); //TODO
		user.put("messageBadges", 0); //TODO
		user.put("notices", new ArrayList<Object>()); //TODO
		user.put("noticeBadges", 0); //TODO
		return user;
	}
	
	/**
	 * 获取当前登录用户所能看见的侧边菜单集合
	 * @param loginUserId
	 * @return
	 */
	protected List<Map<String,Object>> getLoginUserAsideMenuList(Long loginUserId) {
		Long currentClientAccessAppId = (Long)SpringWebMvcUtils.getCurrentHttpServletRequest().getAttribute(SecurityApplicationConstants.CURRENT_CLIENT_ACCESS_APP_ID_KEY);
		List<UpmsResource> userMenuResources = upmsUserService.getUserResourceList(loginUserId, currentClientAccessAppId, UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode());
		if(CollectionUtils.isEmpty(userMenuResources)) {
			userMenuResources = new ArrayList<UpmsResource>();
		}
		return resourceTreeBuilder.buildObjectTree(Arrays.asList(currentClientAccessAppId), userMenuResources, resourceNavMenuNodeConverter);
	}
	
	/**
	 * 获取当前登录用户所能看见的TopNav菜单集合
	 * @param loginUserId
	 * @return
	 */
	protected List<Map<String,Object>> getLoginUserTopnavMenuList(Long loginUserId) {
		List<UpmsResource> userMenuResources = upmsUserService.getUserIndexResourceList(loginUserId);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(!CollectionUtils.isEmpty(userMenuResources)) {
			for(UpmsResource resource : userMenuResources) {
				Map<String,Object> resourceMap = resourceNavMenuNodeConverter.convertTreeNode(resource);
				resourceMap.put("menuName", resource.getAppName());
				resourceMap.put("menuPath", "/" + resource.getAppId());
				resourceNavMenuNodeConverter.setSubTreeNodeList(resourceMap, null);
				resultList.add(resourceMap);
			}
		}
		return resultList;
	}
	
}
