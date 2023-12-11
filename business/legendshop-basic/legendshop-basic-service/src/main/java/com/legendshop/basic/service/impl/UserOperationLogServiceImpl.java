/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.UserOperationLogDao;
import com.legendshop.basic.dto.UserOperationLogDTO;
import com.legendshop.basic.query.UserOperationLogQuery;
import com.legendshop.basic.service.UserOperationLogService;
import com.legendshop.basic.service.convert.UserOperationLogConverter;
import com.legendshop.user.api.OrdinaryUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOperationLogServiceImpl implements UserOperationLogService {

	final UserOperationLogConverter converter;

	final OrdinaryUserApi ordinaryUserApi;

	final UserOperationLogDao userOperationLogDao;

	@Override
	public void save(UserOperationLogDTO log) {
		this.userOperationLogDao.save(this.converter.from(log));
	}

	@Override
	public List<UserOperationLogDTO> findOperationType(String type) {
		return this.userOperationLogDao.findOperationType(type);
	}

	@Override
	public PageSupport<UserOperationLogDTO> findDataPageList(UserOperationLogQuery query) {
		return this.converter.page(this.userOperationLogDao.findDataPageList(query));
	}
}
