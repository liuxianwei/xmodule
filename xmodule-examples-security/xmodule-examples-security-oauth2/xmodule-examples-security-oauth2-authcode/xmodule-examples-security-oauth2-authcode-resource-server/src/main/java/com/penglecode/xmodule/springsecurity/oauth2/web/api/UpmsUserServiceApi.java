package com.penglecode.xmodule.springsecurity.oauth2.web.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.security.oauth2.OAuth2HttpApiResourceSupport;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;
import com.penglecode.xmodule.springsecurity.upms.service.UpmsUserService;

/**
 * UPMS用户服务接口
 * 
 * @author 	pengpeng
 * @date	2019年2月13日 下午6:07:12
 */
@RestController
@RequestMapping("/api/user")
public class UpmsUserServiceApi extends OAuth2HttpApiResourceSupport {

	@Autowired
	private UpmsUserService upmsUserService;
	
	@GetMapping(value="/current", produces=APPLICATION_JSON)
	public Result<UpmsUser> getCurrentUser() {
		UpmsUser currentUser = getAuthenticatedPrincipal();
		return Result.success().message("OK").data(currentUser).build();
	}
	
	/**
	 * 根据用户ID获取用户信息
	 * @param userId
	 * @return
	 */
	@GetMapping(value="/{userId}", produces=APPLICATION_JSON)
	public Result<UpmsUser> getUserById(@PathVariable("userId") Long userId) {
		UpmsUser user = upmsUserService.getUserById(userId);
		return Result.success().message("OK").data(user).build();
	}
	
	/**
	 * 根据用户名获取用户信息
	 * @param userName
	 * @return
	 */
	@GetMapping(value="/name/{userName}", produces=APPLICATION_JSON)
	public Result<UpmsUser> getUserByUserName(@PathVariable("userName") String userName) {
		UpmsUser user = upmsUserService.getUserByUserName(userName);
		return Result.success().message("OK").data(user).build();
	}
	
}
