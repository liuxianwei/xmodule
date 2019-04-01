package com.penglecode.xmodule.clickhouse.samples.jdbc.test;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.clickhouse.ClickHouseDataSource;

public abstract class AbstractJdbcTest {

	protected DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@Before
	public void init() {
		//String url = "jdbc:clickhouse://172.16.96.15:8123/flexedge";
		//String url = "jdbc:clickhouse://172.16.96.57:8123/flexedge";
		//String url = "jdbc:clickhouse://172.16.18.253:8123/flexedge";
		//String url = "jdbc:clickhouse://172.16.96.59:8123/flexedge";
		//String url = "jdbc:clickhouse://192.168.137.119:8123/flexedge";
		String url = "jdbc:clickhouse://47.99.147.15:8123/flexedge";
		String user = "root";
		String password = "123456";
		Properties props = new Properties();
		props.put("user", user);
		props.put("password", password);
		dataSource = new ClickHouseDataSource(url, props);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}
