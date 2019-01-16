package com.penglecode.xmodule.upms.web.security.util;

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
		return (UpmsLoginUser) SpringWebMvcUtils.getCurrentHttpSession().getAttribute(GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY);
	}
	
}
