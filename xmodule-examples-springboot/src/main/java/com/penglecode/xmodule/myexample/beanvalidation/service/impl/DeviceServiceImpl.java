package com.penglecode.xmodule.myexample.beanvalidation.service.impl;

import com.penglecode.xmodule.myexample.beanvalidation.model.Device;
import com.penglecode.xmodule.myexample.beanvalidation.service.DeviceService;

public class DeviceServiceImpl implements DeviceService {

	@Override
	public void createDevice(Device device) {
		System.out.println(">>> create device : " + device);
	}
	
	@Override
	public void updateDevice(Device device) {
		System.out.println(">>> update device : " + device);
	}

}
