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
import com.alibaba.druid.pool.DruidDataSource;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.ZeroIDGen;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.dao.IDAllocDao;
import com.sankuai.inf.leaf.segment.dao.impl.IDAllocDaoImpl;
import com.sankuai.inf.leaf.server.exception.InitException;
import com.sankuai.inf.leaf.server.properties.ConfigProperties;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service("SegmentService")
public class SegmentService implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(SegmentService.class);

	private IDGen idGen;

	private DruidDataSource dataSource;

	@Resource(name = "configProperties")
	ConfigProperties configProperties;

	public Result getId(String key) {
		return idGen.get(key);
	}


	public Results getSegmentIds(String key, int total) {
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

	public SegmentIDGenImpl getIdGen() {
		if (idGen instanceof SegmentIDGenImpl) {
			return (SegmentIDGenImpl) idGen;
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		boolean flag = configProperties.getSegment().isEnable();
		if (flag) {
			//Config dataSource
			dataSource = new DruidDataSource();
			dataSource.setUrl(configProperties.getJdbc().getUrl());
			dataSource.setUsername(configProperties.getJdbc().getUsername());
			dataSource.setPassword(configProperties.getJdbc().getPassword());
			dataSource.init();

			// Config Dao
			IDAllocDao dao = new IDAllocDaoImpl(dataSource);

			// Config ID Gen
			idGen = new SegmentIDGenImpl();
			((SegmentIDGenImpl) idGen).setDao(dao);
			if (idGen.init()) {
				logger.info("Segment Service Init Successfully");
			} else {
				throw new InitException("Segment Service Init Fail");
			}
		} else {
			idGen = new ZeroIDGen();
			logger.info("Zero ID Gen Service Init Successfully");
		}
	}

}
