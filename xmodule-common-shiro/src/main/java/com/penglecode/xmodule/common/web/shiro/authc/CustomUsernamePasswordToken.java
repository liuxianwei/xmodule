package com.penglecode.xmodule.common.web.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomUsernamePasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private final Map<String,Object> attributes = new HashMap<String,Object>();
	
	public CustomUsernamePasswordToken() {
		super();
	}

	public CustomUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public CustomUsernamePasswordToken(String username, char[] password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public CustomUsernamePasswordToken(String username, char[] password, String host) {
		super(username, password, host);
	}

	public CustomUsernamePasswordToken(String username, char[] password) {
		super(username, password);
	}

	public CustomUsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public CustomUsernamePasswordToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public CustomUsernamePasswordToken(String username, String password, String host) {
		super(username, password, host);
	}

	public CustomUsernamePasswordToken(String username, String password) {
		super(username, password);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

}
