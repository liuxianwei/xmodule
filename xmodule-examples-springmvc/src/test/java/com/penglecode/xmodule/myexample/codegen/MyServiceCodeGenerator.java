package com.penglecode.xmodule.myexample.codegen;

import com.penglecode.xmodule.common.codegen.service.ServiceCodeConfig;
import com.penglecode.xmodule.common.codegen.service.ServiceCodeGenerator;
import com.penglecode.xmodule.myexample.model.User;

public class MyServiceCodeGenerator extends ServiceCodeGenerator {

	public static void main(String[] args) throws Exception {
		ServiceCodeConfig config = new ServiceCodeConfig("com.penglecode.xmodule.myexample", "com.penglecode.xmodule.myexample.service", "com.penglecode.xmodule.myexample.service.impl");
		ServiceCodeGenerator serviceCodeGenerator = new MyServiceCodeGenerator();
		serviceCodeGenerator.generateServiceCode(config, User.class);
	}

}
