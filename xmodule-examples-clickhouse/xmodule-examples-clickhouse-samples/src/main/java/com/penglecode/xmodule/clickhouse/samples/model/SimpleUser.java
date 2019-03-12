package com.penglecode.xmodule.clickhouse.samples.model;

import com.penglecode.xmodule.common.support.BaseModel;

public class SimpleUser implements BaseModel<SimpleUser> {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Integer sex;
	
	private String createTime;
	
	private String createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
