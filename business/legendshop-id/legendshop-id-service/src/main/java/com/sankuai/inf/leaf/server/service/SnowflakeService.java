/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.server.service;

import cn.legendshop.jpaplus.exception.IdentifierGenerationException;
import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Results;
import cn.legendshop.jpaplus.model.Status;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.ZeroIDGen;
import com.sankuai.inf.leaf.server.exception.InitException;
import com.sankuai.inf.leaf.server.properties.ConfigProperties;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service("SnowflakeService")
public class SnowflakeService implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(SnowflakeService.class);

	private IDGen idGen;


	@Resource(name = "configProperties")
	ConfigProperties configProperties;

	public Result getId(String key) {
		return idGen.get(key);
	}

	public Results getSnowflakeIds(String key, int total) {
		long[] ids = new long[total];
		for (int i = 0; i < total; i++) {
			Result result = idGen.get(key);
			if (Status.EXCEPTION.equals(result.getStatus())) {
				throw new IdentifierGenerationException("gen id error");
			}
			ids[i] = result.getId();
		}

		return new Results(ids, Status.SUCCESS);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		boolean flag = configProperties.getSnowflake().isEnable();
		if (flag) {
			String zkAddress = configProperties.getSnowflake().getZk().getUrl();
			int port = configProperties.getSnowflake().getPort();
			idGen = new SnowflakeIDGenImpl(zkAddress, port);
			if (idGen.init()) {
				logger.info("Snowflake Service Init Successfully");
			} else {
				throw new InitException("Snowflake Service Init Fail");
			}
		} else {
			idGen = new ZeroIDGen();
			logger.info("Zero ID Gen Service Init Successfully");
		}
	}


}
