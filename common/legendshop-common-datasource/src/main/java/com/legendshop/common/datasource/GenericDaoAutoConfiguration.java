/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.datasource;

import cn.legendshop.jpaplus.EntityFactory;
import cn.legendshop.jpaplus.EntityFactoryImpl;
import cn.legendshop.jpaplus.GenericJdbcDao;
import cn.legendshop.jpaplus.dialect.DialectFactoryBean;
import cn.legendshop.jpaplus.impl.GenericJdbcDaoImpl;
import cn.legendshop.jpaplus.persistence.GeneratorType;
import cn.legendshop.jpaplus.shards.datasource.DataSourceHolder;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid配置数据源的配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(DataSource.class)
public class GenericDaoAutoConfiguration {


	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() {
		return new DruidDataSource();
	}

	@Bean
	public DataSourceHolder dataSourceHolder() {
		Map<String, DataSource> dataSourceMap = new HashMap<>(16);
		dataSourceMap.put("dataSource", mainDataSource());
		DataSourceHolder dataSourceHolder = new DataSourceHolder();
		dataSourceHolder.setDataSourceMap(dataSourceMap);
		log.info("dataSource Auto proxy by legendshop dao! ");
		return dataSourceHolder;
	}


	@Bean("transactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSourceHolder());
		dataSourceTransactionManager.setDefaultTimeout(30000);
		log.info("transactionManager Auto proxy by legendshop dao! ");
		return dataSourceTransactionManager;
	}


	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceHolder());
		jdbcTemplate.setFetchSize(20);
		return jdbcTemplate;
	}

	@Bean
	public EntityFactory entityFactory() {
		EntityFactoryImpl result = new EntityFactoryImpl();
		result.setJdbcTemplate(this.jdbcTemplate());
		result.setShowSQL(Boolean.TRUE);
		result.setShowSelectSQL(Boolean.TRUE);
		result.setUseByteCodeOptimizer(Boolean.FALSE);
		//每次取的步长，只是针对GeneratorType.Local有效
		result.setDefaultAllocationSize(100);
		//采用远程模式，GeneratorType.REMOTE  则使用ID service
		//采用本地模式，GeneratorType.LOCAL
		result.setIdGeneratorType(GeneratorType.REMOTE);
		result.setTransactionManager(transactionManager());
		result.setCachable(Boolean.FALSE);
		return result;
	}

	@Bean
	public GenericJdbcDao genericJdbcDao() {
		GenericJdbcDaoImpl result = new GenericJdbcDaoImpl();
		result.setEntityFactory(this.entityFactory());
		return result;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSourceHolder());
		dataSourceTransactionManager.setDefaultTimeout(30000);
		return dataSourceTransactionManager;
	}

	@Bean
	public StatFilter statfilter() {
		StatFilter statFilter = new StatFilter();
		statFilter.setSlowSqlMillis(500);
		statFilter.setLogSlowSql(Boolean.TRUE);
		statFilter.setMergeSql(Boolean.TRUE);
		return statFilter;
	}


	@Bean
	public DialectFactoryBean dialectFactoryBean() {
		DialectFactoryBean dialectFactoryBean = new DialectFactoryBean();
		dialectFactoryBean.setDialect("cn.legendshop.jpaplus.dialect.Dialect");
		return dialectFactoryBean;
	}


}
