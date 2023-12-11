/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.dao.ExceptionLogDao;
import com.legendshop.basic.dto.ExceptionLogDTO;
import com.legendshop.basic.service.ExceptionLogService;
import com.legendshop.basic.service.convert.ExceptionLogConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (ExceptionLog)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-25 10:20:08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionLogServiceImpl implements ExceptionLogService {

	final ExceptionLogDao exceptionLogDao;

	final ExceptionLogConverter converter;

	@Override
	public Long save(ExceptionLogDTO dto) {
		dto.setUpdateTime(new Date());
		return this.exceptionLogDao.save(this.converter.from(dto));
	}
}
