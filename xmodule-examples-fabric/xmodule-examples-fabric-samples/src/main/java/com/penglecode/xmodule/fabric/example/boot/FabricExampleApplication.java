package com.penglecode.xmodule.fabric.example.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class FabricExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FabricExampleApplication.class, args);
	}
	
}