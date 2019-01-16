package com.penglecode.xmodule.springsecurity.upms.web.security.authc;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsRole;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;
import com.penglecode.xmodule.springsecurity.upms.service.UpmsUserService;

public class DefaultUserDetailsService implements UserDetailsService {

	@Resource(name="upmsUserService")
	private UpmsUserService upmsUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UpmsUser upmsUser = upmsUserService.getUserByUserName(username);
		if(upmsUser == null) {
			throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
		}
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(upmsUser, loginUser);
		
		List<UpmsRole> userRoleList = upmsUserService.getUserRoleListByUserId(upmsUser.getUserId());
		if(!CollectionUtils.isEmpty(userRoleList)) {
			loginUser.setRoleCodes(userRoleList.stream().map(UpmsRole::getRoleCode).collect(Collectors.toList()));
		}
		return loginUser;
	}

}
