package com.penglecode.xmodule.springcloud.consulexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class ConsulExampleConfigAdminApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ConsulExampleConfigAdminApplication.class).run(args);
	}
	
}