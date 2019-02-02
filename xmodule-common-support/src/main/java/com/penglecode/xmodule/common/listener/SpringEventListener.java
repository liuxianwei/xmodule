/**
 * 
 */
package com.penglecode.xmodule.common.listener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动扫描并注册Spring的事件监听器({@link #ApplicationListener})的注解
 * @author 	pengpeng
 * @date	2019年1月28日 上午11:12:26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SpringEventListener {

}
