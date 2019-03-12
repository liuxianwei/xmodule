package com.penglecode.xmodule.clickhouse.samples.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class ClickHouseExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickHouseExampleApplication.class, args);
	}
	
}