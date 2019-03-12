package com.penglecode.xmodule.clickhouse.samples.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.penglecode.xmodule.clickhouse.samples.model.SimpleUser;
import com.penglecode.xmodule.clickhouse.samples.service.SimpleUserService;

@Service("simpleUserService")
public class SimpleUserServiceImpl implements SimpleUserService {

	@Resource(name="clickhouseJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void createUser(SimpleUser user) {

	}

	@Override
	public void updateUserById(SimpleUser user) {

	}

	@Override
	public void deleteUserById(Long id) {

	}

	@Override
	public SimpleUser getUserById(Long id) {
		return null;
	}

	@Override
	public List<SimpleUser> getAllUserList() {
		return null;
	}

}
