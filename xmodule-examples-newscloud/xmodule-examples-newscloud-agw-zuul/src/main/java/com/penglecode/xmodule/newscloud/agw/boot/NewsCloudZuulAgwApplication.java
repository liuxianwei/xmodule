package com.penglecode.xmodule.newscloud.agw.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.penglecode.xmodule.BasePackage;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NewsCloudZuulAgwApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NewsCloudZuulAgwApplication.class).run(args);
	}
	
}
