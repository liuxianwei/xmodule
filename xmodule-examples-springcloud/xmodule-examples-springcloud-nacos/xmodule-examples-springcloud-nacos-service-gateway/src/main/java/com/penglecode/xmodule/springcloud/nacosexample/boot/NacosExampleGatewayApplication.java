package com.penglecode.xmodule.springcloud.nacosexample.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NacosExampleGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosExampleGatewayApplication.class, args);
	}
	
}