package com.penglecode.xmodule.upms.web.security.util;

import javax.servlet.http.HttpSession;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;

/**
 * 统一用户权限管理系统工具类
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午2:42:41
 */
public class UpmsUtils {

	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static UpmsLoginUser getCurrentLoginUser() {
		return getLoginUser(SpringWebMvcUtils.getCurrentHttpSession());
	}
	
	/**
	 * 向当前session中设置登录用户loginUser
	 * @param loginUser
	 */
	public static void setCurrentLoginUser(UpmsLoginUser loginUser) {
		setLoginUser(SpringWebMvcUtils.getCurrentHttpSession(), loginUser);
	}
	
	/**
	 * 从指定HttpSession中获取登录用户信息
	 * @param session
	 * @return
	 */
	public static UpmsLoginUser getLoginUser(HttpSession session) {
		return (UpmsLoginUser) session.getAttribute(GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY);
	}
	
	/**
	 * 向session中设置登录用户loginUser
	 * @param session
	 * @param loginUser
	 */
	public static void setLoginUser(HttpSession session, UpmsLoginUser loginUser) {
		session.setAttribute(GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY, loginUser);
	}
	
}
