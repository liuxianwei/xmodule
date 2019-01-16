package com.penglecode.xmodule.myexample.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.support.CustomMethodValidationPostProcessor;

@Configuration
@ConditionalOnProperty(prefix = "example", name = "running", havingValue = SpringBeanValidationExampleConfiguration.EXAMPLE_APP_NAME)
public class SpringBeanValidationExampleConfiguration extends AbstractSpringConfiguration {

	public static final String EXAMPLE_APP_NAME = "spring_bean_validation";

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
				.addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor processor = new CustomMethodValidationPostProcessor();
		processor.setValidator(validator());
		return processor;
	}

}
