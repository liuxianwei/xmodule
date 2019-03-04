package com.penglecode.xmodule.myexample.codegen;

import com.penglecode.xmodule.common.codegen.mybatis.AbstractMybatisCodeGenerator;

public class MyExampleMybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public MyExampleMybatisCodeGenerator(String generatorConfigFileLocation) {
		super(generatorConfigFileLocation);
	}

	public static void main(String[] args) {
		String configFileLocation = "classpath:mybatis-codegen-config-myexample.xml";
		MyExampleMybatisCodeGenerator generator = new MyExampleMybatisCodeGenerator(configFileLocation);
		generator.run();
	}

}
