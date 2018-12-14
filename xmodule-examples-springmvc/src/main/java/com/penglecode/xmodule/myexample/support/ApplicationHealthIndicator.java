package com.penglecode.xmodule.myexample.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * spring-actuactor的应用监控检测指示器
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午1:32:32
 */
@Component("appHealthIndicator")
public class ApplicationHealthIndicator implements HealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthIndicator.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Health health() {
		Health health = null;
		try {
			String healthSql = "select DATE_FORMAT(NOW(), '%Y-%m-%d %T')";
			jdbcTemplate.queryForObject(healthSql, String.class);
			health = Health.up().build();
		} catch (Throwable e) {
			health = Health.down().withException(e).build();
			LOGGER.error(String.format(">>> 检测应用健康状况发生错误：%s", e.getMessage()), e);
		}
		LOGGER.info(">>> 检测应用健康状况：{}", health);
		return health;
	}
	
}
