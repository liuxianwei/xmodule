package com.penglecode.xmodule.springcloud.nacosexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
@EnableDiscoveryClient
public class NacosExampleProducerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NacosExampleProducerApplication.class).run(args);
	}
	
}
