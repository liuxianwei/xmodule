package com.penglecode.xmodule.common.boot.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.common.support.DefaultDatabase;

/**
 * springboot-mybatis 集成配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:55:15
 */
@Configuration
@ConditionalOnProperty(prefix="spring.mybatis", name={"configLocation", "mapperLocations", "typeAliasesPackage", "mapperScanPackage"})
@ConditionalOnMissingBean(SqlSessionFactory.class)
@EnableTransactionManagement(proxyTargetClass=true)
public class DefaultMybatisConfiguration extends AbstractSpringConfiguration {

	@Bean(name="defaultSqlSessionFactory")
	public SqlSessionFactoryBean defaultSqlSessionFactory(@Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(getDefaultResourcePatternResolver().getResource(getEnvironment().getProperty("spring.mybatis.configLocation")));
		sqlSessionFactoryBean.setTypeAliasesPackage(getEnvironment().getProperty("spring.mybatis.typeAliasesPackage"));
		sqlSessionFactoryBean.setTypeAliasesSuperType(BaseModel.class);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		sqlSessionFactoryBean.setMapperLocations(getDefaultResourcePatternResolver().getResources(getEnvironment().getProperty("spring.mybatis.mapperLocations")));
		return sqlSessionFactoryBean;
	}
	
	@Bean(name="defaultMapperScannerConfigurer")
	public MapperScannerConfigurer defaultMapperScannerConfigurer() throws Exception {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage(getEnvironment().getProperty("spring.mybatis.mapperScanPackage"));
		mapperScannerConfigurer.setAnnotationClass(DefaultDatabase.class);
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("defaultSqlSessionFactory");
		return mapperScannerConfigurer;
	}
	
	@Bean(name="defaultDataSourceTransactionManager")
	public PlatformTransactionManager defaultDataSourceTransactionManager(@Qualifier("defaultDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
