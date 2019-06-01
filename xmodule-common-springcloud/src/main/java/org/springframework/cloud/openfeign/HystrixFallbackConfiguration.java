package org.springframework.cloud.openfeign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;

/**
 * 开启统一的服务熔断|降级配置，
 * 通过动态代理实现统一的fallback逻辑，
 * 没必要每个服务的每个方法都写一堆相同的逻辑的fallback
 * 
 * 需要做如下两步：
 * 
 * 1、注册ProxyHystrixFallbackFactory
 *  @Bean
 *  @Scope("prototype")
 *  public ProxyHystrixFallbackFactory proxyHystrixFallbackFactory() {...}
 *  
 * 2、在@FeignClient注解中使用fallbackFactory=ProxyHystrixFallbackFactory.class
 * 
 * @author 	pengpeng
 * @date	2019年6月1日 下午6:17:50
 */
@Configuration
@ConditionalOnClass(Feign.class)
public class HystrixFallbackConfiguration {

	@Configuration
	@ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
	protected static class HystrixFeignTargeterConfiguration {
		@Bean
		@ConditionalOnMissingBean
		public Targeter feignTargeter() {
			return new HystrixFallbackTargeter();
		}
	}
	
}
