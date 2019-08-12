package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.springcloud.consulexample.model.User;
import com.penglecode.xmodule.springcloud.consulexample.service.api.UserApiService;

@RestController
public class UserController implements UserApiService {

	@Resource(name="userApiService")
	private UserApiService userApiService;

	@Override
	public Result<Long> registerUser(User user) {
		System.out.println(">>> registerUser : " + user);
		return userApiService.registerUser(user);
	}

	@Override
	public Result<Boolean> modifyPassword(User user) {
		System.out.println(">>> modifyPassword : " + user);
		return userApiService.modifyPassword(user);
	}

	@Override
	public Result<User> getUserById(Long userId) {
		System.out.println(">>> getUserById : " + userId);
		return userApiService.getUserById(userId);
	}

	@Override
	public Result<List<User>> getUserList(User condition) {
		System.out.println(">>> getUserList : " + condition);
		return userApiService.getUserList(condition);
	}
	
}
