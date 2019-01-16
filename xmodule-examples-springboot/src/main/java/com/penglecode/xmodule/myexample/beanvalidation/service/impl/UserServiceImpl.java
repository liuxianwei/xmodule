package com.penglecode.xmodule.myexample.beanvalidation.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.penglecode.xmodule.myexample.beanvalidation.model.User;
import com.penglecode.xmodule.myexample.beanvalidation.service.UserService;

@Service("userService")
@Validated
public class UserServiceImpl implements UserService {

	@Override
	public void createUser(User user) {
		System.out.println(">>> createUser : " + user);
	}

	@Override
	public void updateUser(User user) {
		System.out.println(">>> updateUser : " + user);
	}

	@Override
	public void deleteUserById(Long userId) {
		System.out.println(">>> deleteUserById : " + userId);
	}

	@Override
	public void modifyPassword(User user) {
		System.out.println(">>> modifyPassword : " + user);
	}

	@Override
	public String resetPassword(Long userId, String password) {
		System.out.println(">>> modifyPassword : " + userId + " , " + password);
		return null;
	}

}
