package com.penglecode.xmodule.newscenter.codegen;

import com.penglecode.xmodule.common.codegen.service.ServiceCodeConfig;
import com.penglecode.xmodule.common.codegen.service.ServiceCodeGenerator;
import com.penglecode.xmodule.newscenter.model.News;

public class NewsCenterServiceCodeGenerator extends ServiceCodeGenerator {

	public static void main(String[] args) throws Exception {
		String projectBasePackage = "com.penglecode.xmodule.newscenter";
		String serviceInterfacePackage = "com.penglecode.xmodule.newscenter.service";
		String serviceImplementationPackage = "com.penglecode.xmodule.newscenter.service.impl";
		ServiceCodeConfig config = new ServiceCodeConfig(projectBasePackage, serviceInterfacePackage, serviceImplementationPackage);
		ServiceCodeGenerator serviceCodeGenerator = new NewsCenterServiceCodeGenerator();
		serviceCodeGenerator.generateServiceCode(config, News.class);
	}

}
