package com.penglecode.xmodule.springsecurity.upms.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class SimpleUpmsSecurityExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleUpmsSecurityExampleApplication.class, args);
	}
	
}