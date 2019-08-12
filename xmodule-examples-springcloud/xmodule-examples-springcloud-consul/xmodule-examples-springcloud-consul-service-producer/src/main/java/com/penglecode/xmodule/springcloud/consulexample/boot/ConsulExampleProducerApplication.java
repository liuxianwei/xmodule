package com.penglecode.xmodule.springcloud.consulexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
@EnableDiscoveryClient
public class ConsulExampleProducerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ConsulExampleProducerApplication.class).run(args);
	}
	
}
