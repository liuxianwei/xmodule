package com.penglecode.xmodule.clickhouse.samples.jdbc.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.penglecode.xmodule.clickhouse.samples.model.SimpleUser;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.JsonUtils;

public class SimpleUserJdbcTest extends AbstractJdbcTest {

	@Test
	public void insertUser() {
		Date nowTime = new Date();
		String sql = "insert into user(id, name, sex, createTime, createDate) values(?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {4, "赵云", 0, DateTimeUtils.format(nowTime, DateTimeUtils.DEFAULT_DATETIME_PATTERN), DateTimeUtils.format(nowTime, DateTimeUtils.DEFAULT_DATE_PATTERN)});
	}
	
	@Test
	public void deleteUserById() {
		String sql = "delete from user where id = ?";
		jdbcTemplate.update(sql, new Object[] {4});
	}
	
	@Test
	public void selectAllUserList() {
		String sql = "select * from user a order by a.id asc";
		List<SimpleUser> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SimpleUser>(SimpleUser.class));
		if(!CollectionUtils.isEmpty(dataList)) {
			dataList.stream().map(JsonUtils::object2Json).forEach(System.out::println);
		}
	}
	
	@Test
	public void selectUserById() {
		String sql = "select * from user a where a.id = ?";
		SimpleUser user = jdbcTemplate.queryForObject(sql, new Object[] {1}, new BeanPropertyRowMapper<SimpleUser>(SimpleUser.class));
		System.out.println(JsonUtils.object2Json(user));
	}
	
}
