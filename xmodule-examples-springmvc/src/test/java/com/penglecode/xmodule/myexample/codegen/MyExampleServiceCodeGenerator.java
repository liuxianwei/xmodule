package com.penglecode.xmodule.myexample.codegen;

import com.penglecode.xmodule.common.codegen.service.ServiceCodeConfig;
import com.penglecode.xmodule.common.codegen.service.ServiceCodeGenerator;
import com.penglecode.xmodule.myexample.model.Category;
import com.penglecode.xmodule.myexample.model.Product;

public class MyExampleServiceCodeGenerator extends ServiceCodeGenerator {

	public static void main(String[] args) throws Exception {
		String projectBasePackage = "com.penglecode.xmodule.myexample";
		String serviceInterfacePackage = "com.penglecode.xmodule.myexample.service";
		String serviceImplementationPackage = "com.penglecode.xmodule.myexample.service.impl";
		ServiceCodeConfig config = new ServiceCodeConfig(projectBasePackage, serviceInterfacePackage, serviceImplementationPackage);
		ServiceCodeGenerator serviceCodeGenerator = new MyExampleServiceCodeGenerator();
		serviceCodeGenerator.generateServiceCode(config, Product.class, Category.class);
	}

}
