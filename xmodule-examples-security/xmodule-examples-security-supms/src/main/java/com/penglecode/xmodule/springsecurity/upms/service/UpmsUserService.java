package com.penglecode.xmodule.springsecurity.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsRole;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;

public interface UpmsUserService {

	public void createUser(UpmsUser user);
	
	public void updateUser(UpmsUser user);
	
	public void deleteUserById(Long userId);
	
	public void updateUserStatus(Long userId, Integer status);
	
	public UpmsUser getUserById(Long userId);
	
	public UpmsUser getUserByUserName(String userName);
	
	public List<UpmsUser> getUserListByPage(UpmsUser condition, Page page, Sort sort);
	
	public List<UpmsUser> getAllUserList(Integer status);
	
	public List<UpmsRole> getUserRoleListByUserId(Long userId);
	
}
