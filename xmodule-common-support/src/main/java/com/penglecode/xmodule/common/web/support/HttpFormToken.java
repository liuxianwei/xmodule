package com.penglecode.xmodule.common.web.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 防重复提交token注解
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月26日 下午4:31:15
 * @version  	1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HttpFormToken {

	TokenAction action();
	
	String[] key() default {"token"};
	
	/**
	 * 防重复提交token的动作
	 * 
	 * @author	  	pengpeng
	 * @date	  	2014年11月26日 下午4:14:22
	 * @version  	1.0
	 */
	public static enum TokenAction {
		
		/**
		 * 在去下个页面之前创建Token并放入session中
		 */
		CREATE, 
		
		/**
		 * 提交页面的token校验及删除
		 */
		VALIDATE
		
	}
	
}
