package com.penglecode.xmodule.springsecurity.simple.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class SimpleSpringSecurityExample1Application {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringSecurityExample1Application.class, args);
	}
	
}