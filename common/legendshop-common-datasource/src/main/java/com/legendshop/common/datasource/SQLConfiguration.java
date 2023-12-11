/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.datasource;

import cn.legendshop.jpaplus.sql.ConfigCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SQL片段是否采用debug模式
 *
 * @author legendshop
 */
@Component
@Slf4j
public class SQLConfiguration implements InitializingBean {

	/**
	 * 是否启用SQL DEBUG模式，如果是true，则在开发环境中修改了xml中的sql无需重启启动服务，直接生效。
	 */
	@Value("${legendshop.global.environment.sqlDebug:false}")
	private boolean sqlDebug = false;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (sqlDebug) {
			log.info("SQL DEBUG MODE enabled, no need to restart application when SQL in xml file chaned!");
		}

		ConfigCode.getInstance().setDebug(sqlDebug);

	}
}
