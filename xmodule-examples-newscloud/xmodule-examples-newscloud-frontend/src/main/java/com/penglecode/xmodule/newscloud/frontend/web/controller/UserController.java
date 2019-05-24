package com.penglecode.xmodule.newscloud.frontend.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.newscloud.usercenter.model.User;
import com.penglecode.xmodule.newscloud.usercenter.model.UserFollower;
import com.penglecode.xmodule.newscloud.usercenter.service.api.UserApiService;

@RestController
public class UserController extends HttpAPIResourceSupport implements UserApiService {

	@Resource(name="userApiService")
	private UserApiService userApiService;
	
	@Override
	public Result<Long> registerUser(@RequestBody User user) {
		return userApiService.registerUser(user);
	}

	@Override
	public Result<Object> updateUser(@RequestBody User user) {
		return userApiService.updateUser(user);
	}

	@Override
	public Result<Object> deleteUserById(@PathVariable("userId") Long userId) {
		return userApiService.deleteUserById(userId);
	}

	@Override
	public Result<Object> updateUserStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) {
		return userApiService.updateUserStatus(userId, status);
	}

	@Override
	public Result<Object> authorizeUser(@RequestBody User user) {
		return userApiService.authorizeUser(user);
	}

	@Override
	public Result<User> getUserById(@PathVariable("userId") Long userId) {
		return userApiService.getUserById(userId);
	}

	@Override
	public Result<User> getUserByUserName(@PathVariable("userName") String userName) {
		return userApiService.getUserByUserName(userName);
	}

	@Override
	public PageResult<List<User>> getUserListByPage1(User condition, Page page, Sort sort) {
		return userApiService.getUserListByPage1(condition, page, sort);
	}
	
	@Override
	public PageResult<List<User>> getUserListByPage2(Map<String,Object> parameter) {
		return userApiService.getUserListByPage2(parameter);
	}

	@Override
	public Result<List<User>> getUserListByStatus(Boolean authorized, Integer... statuses) {
		return userApiService.getUserListByStatus(authorized, statuses);
	}

	@Override
	public Result<Object> followUser(@RequestBody UserFollower userFollower) {
		return userApiService.followUser(userFollower);
	}

	@Override
	public Result<Object> unfollowUser(@RequestBody UserFollower userFollower) {
		return userApiService.unfollowUser(userFollower);
	}
	
}
