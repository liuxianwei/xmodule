package com.penglecode.xmodule.springcloud.busexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class BusExampleProducerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BusExampleProducerApplication.class).run(args);
	}
	
}
