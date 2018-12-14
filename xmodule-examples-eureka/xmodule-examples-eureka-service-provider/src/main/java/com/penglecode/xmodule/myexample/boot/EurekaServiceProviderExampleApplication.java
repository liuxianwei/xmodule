package com.penglecode.xmodule.myexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.penglecode.xmodule.BasePackage;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class EurekaServiceProviderExampleApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(EurekaServiceProviderExampleApplication.class).run(args);
	}
	
}
