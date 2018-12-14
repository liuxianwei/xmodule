package com.penglecode.xmodule.newscloud.usercenter.consts.em;

public enum SexEnum {

	MALE("M", "男"), FEMALE("F", "女");
	
	private String sexCode;
	
	private String sexName;

	private SexEnum(String sexCode, String sexName) {
		this.sexCode = sexCode;
		this.sexName = sexName;
	}

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	
	public static SexEnum getSex(String sexCode) {
		for(SexEnum em : values()) {
			if(em.getSexCode().equals(sexCode)) {
				return em;
			}
		}
		return null;
	}
	
}
