package com.penglecode.xmodule.springsecurity.upms.mybatis.codegen;

import com.penglecode.xmodule.common.mybatis.codegen.AbstractMybatisCodeGenerator;

public class UpmsMybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public UpmsMybatisCodeGenerator(String generatorConfigFileLocation) {
		super(generatorConfigFileLocation);
	}

	public static void main(String[] args) {
		String configFileLocation = "classpath:mybatis-codegen-config-upms.xml";
		UpmsMybatisCodeGenerator generator = new UpmsMybatisCodeGenerator(configFileLocation);
		generator.run();
	}

}
