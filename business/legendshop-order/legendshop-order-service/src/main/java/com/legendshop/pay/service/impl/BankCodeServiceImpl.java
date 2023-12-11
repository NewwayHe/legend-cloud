/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dao.BankCodeDao;
import com.legendshop.pay.dto.BankCodeDTO;
import com.legendshop.pay.query.BankCodeQuery;
import com.legendshop.pay.service.BankCodeService;
import com.legendshop.pay.service.convert.BankCodeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银行编码(BankCode)表服务实现类
 *
 * @author legendshop
 * @since 2021-04-07 09:56:29
 */
@Service
public class BankCodeServiceImpl implements BankCodeService {

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private BankCodeConverter converter;

	@Override
	public PageSupport<BankCodeDTO> query(BankCodeQuery query) {
		return bankCodeDao.queryBankCode(query);
	}
}
