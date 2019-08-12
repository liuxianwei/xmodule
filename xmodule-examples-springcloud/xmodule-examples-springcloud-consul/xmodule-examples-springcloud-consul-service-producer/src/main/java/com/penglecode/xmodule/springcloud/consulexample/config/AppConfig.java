package com.penglecode.xmodule.springcloud.consulexample.config;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

	private Long id;
	
	private String name;

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
	
	public Map<String,Object> asMap() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}
	
}
