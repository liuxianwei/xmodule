package com.penglecode.xmodule.springcloud.nacosexample.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.context.annotation.Bean;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NacosExampleConfigClientApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NacosExampleConfigClientApplication.class).run(args);
	}
	
	@Bean
	public NacosConfigProperties nacosConfigProperties() {
		return new NacosConfigProperties();
	}
	
}