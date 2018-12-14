package com.penglecode.xmodule.common.web.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * 用户密码加密工具类
 * 
 * @author 	pengpeng
 * @date	2018年10月23日 上午9:43:08
 */
public class UserPasswordUtils {

	private static final PasswordEncoder DEFAULT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	protected static PasswordEncoder getPasswordEncoder() {
		PasswordEncoder passwordEncoder = ApplicationConstants.APPLICATION_CONTEXT.getBean(PasswordEncoder.class);
		return passwordEncoder == null ? DEFAULT_PASSWORD_ENCODER : passwordEncoder;
	}
	
	/**
	 * 明码加密
	 * @param rawPassword
	 * @return
	 */
	public static String encode(String rawPassword) {
		return getPasswordEncoder().encode(rawPassword);
	}
	
	/**
	 * 判断明码与密码是否匹配
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	public static boolean matches(String rawPassword, String encodedPassword) {
		return getPasswordEncoder().matches(rawPassword, encodedPassword);
	}
	
}
