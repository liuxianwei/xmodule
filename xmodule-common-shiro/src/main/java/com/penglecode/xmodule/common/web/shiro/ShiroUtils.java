package com.penglecode.xmodule.common.web.shiro;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * Shiro工具类
 * 
 * @author 	pengpeng
 * @date   		2017年9月14日 下午2:35:30
 * @version 	1.0
 */
public class ShiroUtils {

	/**
	 * 获取当前登录主体
	 * @return
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取Shiro会话
	 * @return
	 */
	public static Session getSession() {
		return getSubject().getSession();
	}
	
	/**
	 * 获取Shiro会话中的attribute值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(String key) {
		return (T) getSession().getAttribute(key);
	}
	
	/**
	 * 设置Shiro会话中的attribute值
	 * @return
	 */
	public static void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	
	/**
	 * 获取用户被登录拦截前的访问URL
	 * @return
	 */
	public static String getSavedRequestUrl(HttpServletRequest request) {
		SavedRequest savedRequest = WebUtils.getSavedRequest(request);
		if(savedRequest != null) {
			return savedRequest.getRequestUrl();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRealm(Class<? extends Realm> realmType){
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		if(!CollectionUtils.isEmpty(securityManager.getRealms())){
			for(Iterator<Realm> it = securityManager.getRealms().iterator(); it.hasNext();){
				Realm realm = it.next();
				if(realm.getClass().equals(realmType)){
					return (T) realm;
				}
			}
		}
		return null;
	}
	
	public static HttpServletRequest getCurrentRequest() {
		WebSubject subject = (WebSubject) getSubject();
		return WebUtils.toHttp(subject.getServletRequest());
	}
	
	public static HttpServletResponse getCurrentResponse() {
		WebSubject subject = (WebSubject) getSubject();
		return WebUtils.toHttp(subject.getServletResponse());
	}
	
}
