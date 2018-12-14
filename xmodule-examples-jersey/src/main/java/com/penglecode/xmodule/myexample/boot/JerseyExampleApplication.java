package com.penglecode.xmodule.myexample.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class JerseyExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JerseyExampleApplication.class, args);
	}
	
}
