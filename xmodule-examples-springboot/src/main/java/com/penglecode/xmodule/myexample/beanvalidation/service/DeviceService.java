package com.penglecode.xmodule.myexample.beanvalidation.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.penglecode.xmodule.myexample.beanvalidation.model.Device;

public interface DeviceService {

	public void createDevice(@Valid @NotNull Device device);
	
	public void updateDevice(Device device);
	
}
