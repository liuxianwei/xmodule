package com.penglecode.xmodule.springcloud.consulexample.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class ConsulExampleGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulExampleGatewayApplication.class, args);
	}
	
}