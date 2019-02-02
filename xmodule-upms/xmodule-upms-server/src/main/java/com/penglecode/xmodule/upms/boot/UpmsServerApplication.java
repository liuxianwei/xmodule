package com.penglecode.xmodule.upms.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class UpmsServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(UpmsServerApplication.class, args);
	}
	
}