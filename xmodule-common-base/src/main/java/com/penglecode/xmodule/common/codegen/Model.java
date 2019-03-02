package com.penglecode.xmodule.common.codegen;

import java.lang.annotation.*;

/**
 * 数据模型注解
 * 
 * @author 	pengpeng
 * @date	2019年3月2日 上午9:28:57
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Model {

	/**
	 * 模型名称
	 * @return
	 */
	String name() default "";
	
}
