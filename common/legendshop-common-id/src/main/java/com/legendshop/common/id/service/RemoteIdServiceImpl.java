/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.id.service;

import cn.legendshop.jpaplus.exception.IdentifierGenerationException;
import cn.legendshop.jpaplus.id.remote.RemoteIdService;
import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Results;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.id.api.IdApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 获取远程Id的服务
 *
 * @author legendshop
 */
@Service
@Slf4j
@Primary
public class RemoteIdServiceImpl implements RemoteIdService {

	@Autowired
	private IdApi idApi;

	@Override
	public Result getSegmentId(String key) {
		R<Result> reslut = idApi.getSegmentId(key);
		if (reslut.getSuccess()) {
			return reslut.getData();
		}

		throw new IdentifierGenerationException("generate id failed with key " + key);
	}

	@Override
	public Results getSegmentIds(String key, int total) {
		R<Results> reslut = idApi.getSegmentIds(key, total);
		if (reslut.getSuccess()) {
			return reslut.getData();
		}

		throw new IdentifierGenerationException("generate id failed with key " + key);
	}

	@Override
	public Result getSnowflakeId(String key) {
		R<Result> reslut = idApi.getSnowflakeId(key);
		if (reslut.getSuccess()) {
			return reslut.getData();
		}
		throw new IdentifierGenerationException("generate id failed with key " + key);
	}

	@Override
	public Results getSnowflakeIds(String key, int total) {
		R<Results> reslut = idApi.getSnowflakeIds(key, total);
		if (reslut.getSuccess()) {
			return reslut.getData();
		}
		throw new IdentifierGenerationException("generate id failed with key " + key);
	}
}
