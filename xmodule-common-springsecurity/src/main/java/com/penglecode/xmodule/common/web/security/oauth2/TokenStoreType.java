package com.penglecode.xmodule.common.web.security.oauth2;

/**
 * memory/redis/jwt
 * 
 * @author 	pengpeng
 * @date	2019年2月19日 下午1:26:49
 */
public enum TokenStoreType {

	MEMORY("memory"), REDIS("redis"), JWT("jwt");
	
	private String typeCode;

	private TokenStoreType(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
}
