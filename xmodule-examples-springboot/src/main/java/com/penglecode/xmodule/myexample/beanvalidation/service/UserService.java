package com.penglecode.xmodule.myexample.beanvalidation.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Create;
import com.penglecode.xmodule.myexample.beanvalidation.model.User;

public interface UserService {

	@Validated({ConstraintOrder.class, Create.class})
	public void createUser(@Valid @NotNull(message="参数不能为空!") User user);
	
	public void updateUser(User user);
	
	public void deleteUserById(Long userId);
	
	public void modifyPassword(User user);
	
	public String resetPassword(Long userId, String password);
	
}
