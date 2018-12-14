package com.penglecode.xmodule.springsecurity.upms.service;

import java.util.List;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsRole;

public interface UpmsRoleService {

	public void createRole(UpmsRole role);
	
	public void updateRole(UpmsRole role);
	
	public void deleteRoleById(Long roleId);
	
	public UpmsRole getRoleById(Long roleId);
	
	public List<UpmsRole> getRoleListByPage(UpmsRole condition, Page page, Sort sort);
	
	public List<UpmsRole> getAllRoleList();
	
}
