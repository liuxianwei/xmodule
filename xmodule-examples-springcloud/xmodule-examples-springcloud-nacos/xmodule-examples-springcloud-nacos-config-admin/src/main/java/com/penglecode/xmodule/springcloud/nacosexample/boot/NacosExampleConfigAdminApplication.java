package com.penglecode.xmodule.springcloud.nacosexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NacosExampleConfigAdminApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NacosExampleConfigAdminApplication.class).run(args);
	}
	
}