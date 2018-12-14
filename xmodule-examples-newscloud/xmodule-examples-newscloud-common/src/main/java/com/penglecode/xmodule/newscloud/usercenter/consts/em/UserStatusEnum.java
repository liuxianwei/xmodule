package com.penglecode.xmodule.newscloud.usercenter.consts.em;

public enum UserStatusEnum {

	ENABLED(1, "正常"), DISABLED(0, "禁用"), SILENT(2, "禁言");
	
	private Integer statusCode;
	
	private String statusName;

	private UserStatusEnum(Integer statusCode, String statusName) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public static UserStatusEnum getStatus(Integer statusCode) {
		for(UserStatusEnum em : values()) {
			if(em.getStatusCode().equals(statusCode)) {
				return em;
			}
		}
		return null;
	}
	
}
