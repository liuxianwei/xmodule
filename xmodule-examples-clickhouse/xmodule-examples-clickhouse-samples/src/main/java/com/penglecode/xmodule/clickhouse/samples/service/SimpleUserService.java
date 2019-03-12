package com.penglecode.xmodule.clickhouse.samples.service;

import java.util.List;

import com.penglecode.xmodule.clickhouse.samples.model.SimpleUser;

public interface SimpleUserService {

	public void createUser(SimpleUser user);
	
	public void updateUserById(SimpleUser user);
	
	public void deleteUserById(Long id);
	
	public SimpleUser getUserById(Long id);
	
	public List<SimpleUser> getAllUserList();
	
}
