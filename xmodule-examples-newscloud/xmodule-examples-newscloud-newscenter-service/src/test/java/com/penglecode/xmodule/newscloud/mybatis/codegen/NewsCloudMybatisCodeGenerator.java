package com.penglecode.xmodule.newscloud.mybatis.codegen;

import com.penglecode.xmodule.common.codegen.mybatis.AbstractMybatisCodeGenerator;

public class NewsCloudMybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public NewsCloudMybatisCodeGenerator(String generatorConfigFileLocation) {
		super(generatorConfigFileLocation);
	}

	public static void main(String[] args) {
		String configFileLocation = "classpath:mybatis-codegen-config-newscloud.xml";
		NewsCloudMybatisCodeGenerator generator = new NewsCloudMybatisCodeGenerator(configFileLocation);
		generator.run();
	}

}
