package com.penglecode.xmodule.common.support;

import java.io.Serializable;

public class VersionedData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String version;
	
	private T data;

	public VersionedData() {
		super();
	}

	public VersionedData(String version, T data) {
		super();
		this.version = version;
		this.data = data;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
