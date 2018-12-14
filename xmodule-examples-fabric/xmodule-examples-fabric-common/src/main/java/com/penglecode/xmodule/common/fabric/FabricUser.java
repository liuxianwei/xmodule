package com.penglecode.xmodule.common.fabric;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Fabric用户
 * 
 * @author 	pengpeng
 * @date	2018年11月7日 下午5:11:43
 */
public class FabricUser implements User, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
    private Set<String> roles;
    
    private String account;
    
    private String affiliation;
    
    @JsonDeserialize(as=FabricEnrollment.class)
    private Enrollment enrollment;
    
    private String mspId;
    
    @JsonIgnore
    private transient String secret;

	public FabricUser() {
		super();
	}
	
	public FabricUser(String name, String affiliation, String mspId, Enrollment enrollment) {
        this.setName(name);
        this.setAffiliation(affiliation);
        this.setEnrollment(enrollment);
        this.setMspId(mspId);
    }

	public FabricUser(String name, Set<String> roles, String account, String affiliation, Enrollment enrollment,
			String mspId) {
		super();
		this.setName(name);
        this.setAffiliation(affiliation);
        this.setEnrollment(enrollment);
        this.setMspId(mspId);
        this.setRoles(roles);
        this.setAccount(account);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		if(enrollment instanceof FabricEnrollment) {
			this.enrollment = (FabricEnrollment) enrollment;
		} else {
			this.enrollment = new FabricEnrollment(enrollment);
		}
	}

	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String primaryKey() {
		if(name == null || name.length() == 0) {
			throw new IllegalStateException("Property 'name' can not be empty!");
		}
		if(affiliation == null || affiliation.length() == 0) {
			throw new IllegalStateException("Property 'affiliation' can not be empty!");
		}
		return name + "@" + affiliation;
	}
	
	@Override
	public String toString() {
		return "FabricUser [name=" + name + ", roles=" + roles + ", account=" + account + ", affiliation=" + affiliation
				+ ", enrollment=" + enrollment + ", mspId=" + mspId + "]";
	}
	
}
