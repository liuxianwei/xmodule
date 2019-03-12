package com.penglecode.xmodule.clickhouse.samples.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

/**
 * springboot-druid多数据源配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:54:23
 */
@Configuration
@ConditionalOnClass(ClickHouseDataSource.class)
@ConditionalOnProperty(prefix="spring.clickhouse.standalone", name="enabled", havingValue="true")
public class ClickHouseStandaloneConfiguration extends AbstractSpringConfiguration {

	@Bean(name="clickhouseProperties")
	@ConfigurationProperties(prefix = "spring.clickhouse.standalone.datasource")
	public ClickHouseProperties clickhouseProperties() {
		return new ClickHouseProperties();
	}
	
	@Bean(name="clickhouseDataSource")
	public ClickHouseDataSource clickhouseDataSource(ClickHouseProperties clickhouseProperties) {
		String url = getEnvironment().getProperty("spring.clickhouse.standalone.datasource.url");
		return new ClickHouseDataSource(url, clickhouseProperties);
	}
	
	@Bean(name="clickhouseJdbcTemplate")
	public JdbcTemplate defaultJdbcTemplate(ClickHouseDataSource dataSource) throws Exception {
		return new JdbcTemplate(dataSource);
	}
	
}