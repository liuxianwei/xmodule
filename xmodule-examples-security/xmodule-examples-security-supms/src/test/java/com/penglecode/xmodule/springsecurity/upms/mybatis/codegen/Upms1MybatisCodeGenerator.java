package com.penglecode.xmodule.springsecurity.upms.mybatis.codegen;

import com.penglecode.xmodule.common.mybatis.codegen.AbstractMybatisCodeGenerator;

public class Upms1MybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public Upms1MybatisCodeGenerator(String generatorConfigFileLocation) {
		super(generatorConfigFileLocation);
	}

	public static void main(String[] args) {
		String configFileLocation = "classpath:mybatis-codegen-config-upms.xml";
		Upms1MybatisCodeGenerator generator = new Upms1MybatisCodeGenerator(configFileLocation);
		generator.run();
	}

}
