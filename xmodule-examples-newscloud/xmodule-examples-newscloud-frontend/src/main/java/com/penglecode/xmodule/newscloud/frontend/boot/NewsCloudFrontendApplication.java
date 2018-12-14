package com.penglecode.xmodule.newscloud.frontend.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.penglecode.xmodule.BasePackage;

import feign.Logger;

@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NewsCloudFrontendApplication {

	@Bean
	public Logger.Level feignLoggerLevel(){
		return  Logger.Level.FULL;
	}
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(NewsCloudFrontendApplication.class).run(args);
	}
	
}
