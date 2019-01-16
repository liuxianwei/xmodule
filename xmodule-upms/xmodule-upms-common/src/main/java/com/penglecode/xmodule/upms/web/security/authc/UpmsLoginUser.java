package com.penglecode.xmodule.upms.web.security.authc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.penglecode.xmodule.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.upms.model.UpmsUser;

/**
 * 基于spring-security的登录用户
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午2:34:03
 */
public class UpmsLoginUser extends UpmsUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private List<String> roleCodes;
    
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
    	Collection<GrantedAuthority> authorities = null;
		if(roleCodes != null && !roleCodes.isEmpty()) {
			authorities = new LinkedHashSet<>();
			for(String roleCode : roleCodes) {
				GrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return UpmsUserStatusEnum.USER_STATUS_ENABLED.getStatusCode().equals(getStatus());
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public Map<String,Object> asMap() {
		Map<String,Object> data = new LinkedHashMap<String,Object>();
		data.put("userId", getUserId());
		data.put("userName", getUserName());
		data.put("realName", getRealName());
		data.put("nickName", getNickName());
		data.put("mobilePhone", getMobilePhone());
		data.put("email", getEmail());
		data.put("userIcon", getUserIcon());
		data.put("userIconUrl", getUserIconUrl());
		data.put("userType", getUserType());
		data.put("userTypeName", getUserTypeName());
		data.put("status", getStatus());
		data.put("statusName", getStatusName());
		data.put("lastLoginTime", getLastLoginTime());
		data.put("loginTimes", getLoginTimes());
		data.put("createTime", getCreateTime());
		data.put("createBy", getCreateBy());
		data.put("updateTime", getUpdateTime());
		data.put("updateBy", getUpdateBy());
		return data;
	}
	
	public String toString() {
		return asMap().toString();
	}
	
}
