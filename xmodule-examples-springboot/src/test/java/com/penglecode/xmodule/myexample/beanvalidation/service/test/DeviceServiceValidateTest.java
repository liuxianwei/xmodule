package com.penglecode.xmodule.myexample.beanvalidation.service.test;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Create;
import com.penglecode.xmodule.myexample.beanvalidation.model.Device;
import com.penglecode.xmodule.myexample.beanvalidation.service.DeviceService;
import com.penglecode.xmodule.myexample.beanvalidation.service.impl.DeviceServiceImpl;

public class DeviceServiceValidateTest {

	private static Validator validator;
	
	private static DeviceService deviceService;

    @BeforeClass
    public static void setUpValidator() {
	    // 通过工厂获取到一个Validator的实例
    	ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
				.addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();
        validator = validatorFactory.getValidator();
        deviceService = new DeviceServiceImpl();
    }
    
    @Test
    public void validate4CreateDevice() {
    	for(int i = 0; i < 1; i++) {
    		Device device = new Device();
        	device.setDeviceId(null);
        	device.setDeviceName("");
        	device.setDeviceNo("");
        	
        	Method method = ReflectionUtils.findMethod(DeviceService.class, "createDevice", new Class<?>[] {Device.class});
        	Object[] parameterValues = new Object[] {device};
        	Class<?>[] groups = new Class<?>[] {ConstraintOrder.class, Create.class};
        	Set<ConstraintViolation<DeviceService>> constraintViolations = validator.forExecutables().validateParameters(deviceService, method, parameterValues, groups);
        	constraintViolations.stream().forEach(cv -> {
        		System.err.println(cv.getPropertyPath() + ": " + cv.getMessage());
        	});
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
    	}
    }
	
}
