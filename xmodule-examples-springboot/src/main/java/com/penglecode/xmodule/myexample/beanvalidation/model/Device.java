package com.penglecode.xmodule.myexample.beanvalidation.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint1;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint2;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint3;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Create;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Update;

public class Device implements BaseModel<Device> {

	private static final long serialVersionUID = 1L;

	@Null(message="设备ID必须为空!", groups={Create.class})
	@NotNull(message="设备ID不能为空!", groups={Update.class})
	@Positive(message="设备ID不合法!", groups={Update.class})
	private Long deviceId;
	
	@NotEmpty(message="设备名称不能为空!", groups={Create.class, Update.class})
	private String deviceName;
	
	@NotEmpty(message="设备编号不能为空!", groups={Constraint3.class, Create.class, Update.class})
	@Pattern(regexp="\\d{4}\\-\\d{4}\\-\\d4", message="设备编号不合法!", groups={Constraint2.class, Create.class, Update.class})
	@Size(min=15, max=15, message="设备编号长度有且仅有15位字符!", groups={Constraint1.class, Create.class, Update.class})
	private String deviceNo;
	
	@NotEmpty(message="设备IP不能为空!")
	@Pattern(regexp="\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", message="设备IP不合法!")
	private String deviceIp;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	
}
