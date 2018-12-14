package com.penglecode.xmodule.common.initializer;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

public abstract class SpringAppPostInitializer<T extends ConfigurableApplicationContext> extends AbstractSpringAppInitializer<T> implements Ordered {

	@Override
	public int getOrder() {
		return 0;
	}

}
