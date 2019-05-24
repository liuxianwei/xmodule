package com.penglecode.xmodule.newscloud.usercenter.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.penglecode.xmodule.BasePackage;

@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NewsCloudUserCenterApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NewsCloudUserCenterApplication.class).run(args);
	}
	
}
