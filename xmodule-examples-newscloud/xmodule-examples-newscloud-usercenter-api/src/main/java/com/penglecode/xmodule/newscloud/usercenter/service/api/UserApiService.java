package com.penglecode.xmodule.newscloud.usercenter.service.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.newscloud.usercenter.model.User;
import com.penglecode.xmodule.newscloud.usercenter.model.UserFollower;

/**
 * 用户API服务
 * 
 * @author 	pengpeng
 * @date	2018年10月8日 下午2:43:58
 */
@FeignClient(name="NEWSCLOUD-USERCENTER-SERVICE", qualifier="userApiService")
public interface UserApiService {

	/**
	 * 创建用户
	 * @param user
	 */
	@PostMapping(value="/api/user/register", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Long> registerUser(@RequestBody User user);
	
	/**
	 * 修改用户
	 * @param user
	 */
	@PutMapping(value="/api/user/update", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> updateUser(@RequestBody User user);
	
	/**
	 * 删除用户
	 * @param userId
	 */
	@DeleteMapping(value="/api/user/delete/{userId}", produces=APPLICATION_JSON)
	public Result<Object> deleteUserById(@PathVariable("userId") Long userId);
	
	/**
	 * 更新用户状态
	 * @param userId
	 * @param status
	 */
	@PutMapping(value="/api/user/{userId}/status/{status}", produces=APPLICATION_JSON)
	public Result<Object> updateUserStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status);
	
	/**
	 * 用户认证
	 * @param user
	 */
	@PostMapping(value="/api/user/authorize", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> authorizeUser(@RequestBody User user);
	
	/**
	 * 根据userId获取用户信息
	 * @param userId
	 * @return
	 */
	@GetMapping(value="/api/user/{userId}", produces=APPLICATION_JSON)
	public Result<User> getUserById(@PathVariable("userId") Long userId);
	
	/**
	 * 根据userName获取用户信息
	 * @param userName
	 * @return
	 */
	@GetMapping(value="/api/user/username/{userName}", produces=APPLICATION_JSON)
	public Result<User> getUserByUserName(@PathVariable("userName") String userName);
	
	/**
	 * 根据条件查询用户列表(分页、排序)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	@GetMapping(value="/api/user/list1", produces=APPLICATION_JSON)
	public PageResult<List<User>> getUserListByPage1(@RequestParam User condition, @RequestParam Page page, @RequestParam Sort sort);
	
	/**
	 * 根据条件查询用户列表(分页、排序)
	 * @param statuses
	 * @return
	 */
	@GetMapping(value="/api/user/list2", produces=APPLICATION_JSON)
	public PageResult<List<User>> getUserListByPage2(@RequestParam Map<String,Object> parameter);
	
	/**
	 * 根据状态获取用户列表
	 * @param statuses
	 * @return
	 */
	@GetMapping(value="/api/user/list/bystatus", produces=APPLICATION_JSON)
	public Result<List<User>> getUserListByStatus(@RequestParam Boolean authorized, @RequestParam Integer... statuses);
	
	/**
	 * 用户关注他人
	 * @param userFollower
	 */
	@PostMapping(value="/api/user/follow", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> followUser(@RequestBody UserFollower userFollower);
	
	/**
	 * 用户取消关注他人
	 * @param userFollower
	 */
	@PostMapping(value="/api/user/unfollow", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> unfollowUser(@RequestBody UserFollower userFollower);
	
}
