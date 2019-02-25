package com.penglecode.xmodule.springsecurity.oauth2.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class OAuth2AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2AuthorizationServerApplication.class, args);
	}
	
}