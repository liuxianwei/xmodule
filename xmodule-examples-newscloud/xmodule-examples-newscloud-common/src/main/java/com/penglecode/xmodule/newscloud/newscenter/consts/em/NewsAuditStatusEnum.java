package com.penglecode.xmodule.newscloud.newscenter.consts.em;

public enum NewsAuditStatusEnum {

	AUDIT_WAITING(0, "待审核"), AUDIT_PASS(1, "审核通过"), AUDIT_FAIL(2, "审核不通过");
	
	private Integer statusCode;
	
	private String statusName;

	private NewsAuditStatusEnum(Integer statusCode, String statusName) {
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
	
	public static NewsAuditStatusEnum getStatus(Integer statusCode) {
		for(NewsAuditStatusEnum em : values()) {
			if(em.getStatusCode().equals(statusCode)) {
				return em;
			}
		}
		return null;
	}
	
}
