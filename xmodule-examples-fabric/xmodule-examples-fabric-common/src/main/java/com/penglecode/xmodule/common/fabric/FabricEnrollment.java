package com.penglecode.xmodule.common.fabric;

import java.io.Serializable;
import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.penglecode.xmodule.common.fabric.support.PrivateKeyJsonDeserializer;
import com.penglecode.xmodule.common.fabric.support.PrivateKeyJsonSerializer;

/**
 * 认证登记
 * 
 * @author 	pengpeng
 * @date	2018年11月7日 下午5:10:37
 */
public class FabricEnrollment implements Enrollment, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonDeserialize(using=PrivateKeyJsonDeserializer.class)
	@JsonSerialize(using=PrivateKeyJsonSerializer.class)
	private PrivateKey key;
	
    private String cert;
	
    public FabricEnrollment() {
		super();
	}

	public FabricEnrollment(Enrollment enrollment) {
    	this(enrollment.getKey(), enrollment.getCert());
    }
    
	public FabricEnrollment(PrivateKey key, String cert) {
		super();
		this.key = key;
		this.cert = cert;
	}

	public PrivateKey getKey() {
		return key;
	}

	public void setKey(PrivateKey key) {
		this.key = key;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

}
