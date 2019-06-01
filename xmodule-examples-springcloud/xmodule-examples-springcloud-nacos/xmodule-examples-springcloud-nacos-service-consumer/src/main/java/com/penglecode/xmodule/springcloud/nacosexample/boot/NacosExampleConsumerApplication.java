package com.penglecode.xmodule.springcloud.nacosexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
@EnableFeignClients(basePackageClasses=BasePackage.class)
@EnableDiscoveryClient
public class NacosExampleConsumerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NacosExampleConsumerApplication.class).run(args);
	}
	
}
