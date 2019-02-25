package com.penglecode.xmodule.springsecurity.oauth2.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class WechatOAuth2ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatOAuth2ExampleApplication.class, args);
	}
	
}