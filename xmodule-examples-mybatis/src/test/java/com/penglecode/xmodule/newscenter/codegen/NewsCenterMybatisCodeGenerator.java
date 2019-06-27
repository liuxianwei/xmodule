package com.penglecode.xmodule.newscenter.codegen;

import com.penglecode.xmodule.common.codegen.mybatis.AbstractMybatisCodeGenerator;

public class NewsCenterMybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public NewsCenterMybatisCodeGenerator(String generatorConfigFileLocation) {
		super(generatorConfigFileLocation);
	}

	public static void main(String[] args) {
		String configFileLocation = "classpath:mybatis-codegen-config-newscenter.xml";
		NewsCenterMybatisCodeGenerator generator = new NewsCenterMybatisCodeGenerator(configFileLocation);
		generator.run();
	}

}
