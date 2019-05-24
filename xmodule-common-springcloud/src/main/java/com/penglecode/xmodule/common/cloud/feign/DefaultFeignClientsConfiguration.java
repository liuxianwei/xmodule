package com.penglecode.xmodule.common.cloud.feign;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

/**
 * 默认的Feign自定义配置
 * 
 * @see #FeignClientsConfiguration
 * 
 * @author 	pengpeng
 * @date	2019年5月22日 下午5:45:06
 */
@Configuration
@ConditionalOnClass(SpringMvcContract.class)
public class DefaultFeignClientsConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer {

	@Bean
	public List<FeignFormatterRegistrar> feignFormatterRegistrar() {
		return Arrays.asList(new DefaultFeignFormatterRegistrar());
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToObjectRequestParamConverter());
	}

	public static class DefaultFeignFormatterRegistrar implements FeignFormatterRegistrar {
		
		@Override
		public void registerFormatters(FormatterRegistry registry) {
			registry.addConverter(new ObjectRequestParamToStringConverter());
		}
		
	}
	
}
