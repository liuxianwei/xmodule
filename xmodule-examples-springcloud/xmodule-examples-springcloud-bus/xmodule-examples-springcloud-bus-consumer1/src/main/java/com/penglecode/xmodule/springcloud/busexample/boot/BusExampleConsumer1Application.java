package com.penglecode.xmodule.springcloud.busexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class BusExampleConsumer1Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BusExampleConsumer1Application.class).run(args);
	}
	
}
