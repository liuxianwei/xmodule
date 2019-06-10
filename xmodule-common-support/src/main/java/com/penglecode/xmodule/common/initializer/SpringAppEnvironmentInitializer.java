package com.penglecode.xmodule.common.initializer;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.penglecode.xmodule.common.consts.SpringEnvConstantPool;

/**
 * Spring应用的环境变量初始化
 * 
 * @author 	pengpeng
 * @date	2019年6月6日 下午3:52:40
 */
public class SpringAppEnvironmentInitializer implements EnvironmentAware {

	@Override
	public void setEnvironment(Environment environment) {
		SpringEnvConstantPool.setEnvironment(environment);
	}

}
