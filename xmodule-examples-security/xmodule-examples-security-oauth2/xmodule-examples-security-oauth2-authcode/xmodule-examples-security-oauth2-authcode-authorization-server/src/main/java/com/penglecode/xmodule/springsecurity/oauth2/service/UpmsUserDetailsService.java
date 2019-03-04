package com.penglecode.xmodule.springsecurity.oauth2.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;
import com.penglecode.xmodule.springsecurity.upms.service.UpmsUserService;
import com.penglecode.xmodule.springsecurity.upms.support.UpmsLoginUser;

public class UpmsUserDetailsService implements UserDetailsService {

	@Autowired
	private UpmsUserService upmsUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UpmsUser upmsUser = upmsUserService.getUserByUserName(username);
		if(upmsUser == null) {
			throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
		}
		UpmsLoginUser loginUser = new UpmsLoginUser(upmsUser);
		loginUser.setRoleCodes(Arrays.asList("ROLE_USER"));
		return loginUser;
	}
	
}
