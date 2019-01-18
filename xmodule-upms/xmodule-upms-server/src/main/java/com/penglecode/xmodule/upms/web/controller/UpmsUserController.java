package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.model.UpmsUser;
import com.penglecode.xmodule.upms.service.UpmsUserService;
import com.penglecode.xmodule.upms.support.XUploadFileHelper;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * UPMS用户接口
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午1:52:37
 */
@RestController
@RequestMapping("/user")
public class UpmsUserController extends HttpAPIResourceSupport {

	private UpmsUserService upmsUserService;
	
	/**
	 * 查询用户列表(排序)
	 * @param condition		- 查询条件参数
	 * @param sort			- 排序参数
	 * @return
	 */
	@GetMapping(value="/list", produces=APPLICATION_JSON)
	public Result<List<UpmsUser>> getUserList(UpmsUser condition, Page page, Sort sort) {
		List<UpmsUser> dataList = upmsUserService.getUserListByPage(condition, page, sort);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 创建用户
	 * @param user
	 * @return
	 */
	@PostMapping(value="/create", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> createUser(@RequestBody UpmsUser user) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		user.setCreateBy(loginUser.getUserId());
		if(!StringUtils.isEmpty(user.getUserIcon())){
			String userIcon = user.getUserIcon();
			if(userIcon.toLowerCase().startsWith(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH)){
				String contextRealPath = SpringWebMvcUtils.getCurrentHttpSession().getServletContext().getRealPath("/");
				userIcon = XUploadFileHelper.transferUserIconToLocalDir(contextRealPath, userIcon);
				user.setUserIcon(userIcon);
			}
		}
		user.setUserIcon(StringUtils.defaultIfEmpty(user.getUserIcon(), GlobalConstants.DEFAULT_USER_AVATAR));
		upmsUserService.createUser(user);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	@PutMapping(value="/update", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> updateUser(@RequestBody UpmsUser user) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		user.setUpdateBy(loginUser.getUserId());
		
		if(!StringUtils.isEmpty(user.getUserIcon())){
			String userIcon = user.getUserIcon();
			if(userIcon.toLowerCase().startsWith(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH)){
				String contextRealPath = SpringWebMvcUtils.getCurrentHttpSession().getServletContext().getRealPath("/");
				userIcon = XUploadFileHelper.transferUserIconToLocalDir(contextRealPath, userIcon);
				user.setUserIcon(userIcon);
			}
		}
		user.setUserIcon(StringUtils.defaultIfEmpty(user.getUserIcon(), GlobalConstants.DEFAULT_USER_AVATAR));
		upmsUserService.updateUser(user);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据用户ID删除用户
	 * @param userId
	 * @return
	 */
	@DeleteMapping(value="/delete/{userId}", produces=APPLICATION_JSON)
	public Result<Object> deleteUserById(@PathVariable("userId") Long userId) {
		upmsUserService.deleteUserById(userId);
		return Result.success().message("删除成功!").build();
	}
	
	/**
	 * 查询用户所拥有的角色列表
	 * @param userId
	 * @param appId
	 * @param condition
	 * @return
	 */
	@GetMapping(value="/{userId}/roles", produces=APPLICATION_JSON)
	public Result<List<UpmsRole>> getUserRoles(@PathVariable("userId") Long userId, Long appId, UpmsRole condition) {
		List<UpmsRole> dataList = upmsUserService.getUserRoleList(userId, appId, condition);
		return Result.success().message("OK").data(dataList).build();
	}
	
	/**
	 * 添加用户-角色配置
	 * @param parameter
	 * @return
	 */
	@PostMapping(value="/roles/add", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> addUserRoles(@RequestBody Map<String,Object> parameter) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		Long userId = MapUtils.getLong(parameter, "userId");
		String roleIds = MapUtils.getString(parameter, "roleIds");
		List<Long> roleIdList = new ArrayList<Long>();
		if(!StringUtils.isEmpty(roleIds)){
			roleIdList = Stream.of(roleIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
		}
		UpmsUser param = new UpmsUser();
		param.setUserId(userId);
		upmsUserService.addUserRoles(param, roleIdList, loginUser.getUserId(), DateTimeUtils.formatNow());
		return Result.success().message("添加成功!").build();
	}
	
	/**
	 * 删除用户-角色配置
	 * @param parameter
	 * @return
	 */
	@DeleteMapping(value="/roles/del", consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Result<Object> delUserRoles(@RequestBody Map<String,Object> parameter) {
		Long userId = MapUtils.getLong(parameter, "userId");
		String roleIds = MapUtils.getString(parameter, "roleIds");
		List<Long> roleIdList = new ArrayList<Long>();
		if(!StringUtils.isEmpty(roleIds)){
			roleIdList = Stream.of(roleIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
		}
		UpmsUser param = new UpmsUser();
		param.setUserId(userId);
		upmsUserService.delUserRoles(param, roleIdList);
		return Result.success().message("删除成功!").build();
	}
	
	/**
	 * 启用用户
	 * @param userId
	 * @return
	 */
	@PutMapping(value="/enable/{userId}", produces=APPLICATION_JSON)
	public Result<Object> enableUser(@PathVariable("userId") Long userId) {
		return updateUserStatus(userId, UpmsUserStatusEnum.USER_STATUS_ENABLED);
	}
	
	/**
	 * 禁用用户
	 * @param userId
	 * @return
	 */
	@PutMapping(value="/disable/{userId}", produces=APPLICATION_JSON)
	public Result<Object> disableUser(@PathVariable("userId") Long userId) {
		return updateUserStatus(userId, UpmsUserStatusEnum.USER_STATUS_DISABLED);
	}
	
	protected Result<Object> updateUserStatus(Long userId, UpmsUserStatusEnum targetStatus){
		upmsUserService.updateUserStatus(userId, targetStatus.getStatusCode());
		return Result.success().message(targetStatus.getStatusName() + "成功!").build();
	}
	
	/**
	 * 用户自己修改密码
	 * @param parameter
	 * @return
	 */
	@PutMapping(value="/changepwd", produces=APPLICATION_JSON)
	public Result<Object> userChangePasswd(@RequestBody UpmsUser parameter) {
		upmsUserService.updatePassword(parameter, Boolean.FALSE);
		return Result.success().message("修改成功!").build();
	}
	
	/**
	 * 管理员强制修改用户密码
	 * @param parameter
	 * @return
	 */
	@PutMapping(value="/changepwd/force", produces=APPLICATION_JSON)
	public Result<Object> changeUserPasswd(@RequestBody UpmsUser parameter) {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		parameter.setUserId(loginUser.getUserId());
		upmsUserService.updatePassword(parameter, Boolean.TRUE);
		return Result.success().message("修改成功!").build();
	}
	
}
