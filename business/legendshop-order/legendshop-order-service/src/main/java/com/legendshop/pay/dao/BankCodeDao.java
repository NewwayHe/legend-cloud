/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dto.BankCodeDTO;
import com.legendshop.pay.entity.BankCode;
import com.legendshop.pay.query.BankCodeQuery;

/**
 * 银行编码(BankCode)表数据库访问层
 *
 * @author legendshop
 * @since 2021-04-07 09:56:29
 */
public interface BankCodeDao extends GenericDao<BankCode, Long> {

	/**
	 * 查询银行编码
	 *
	 * @param query
	 * @return
	 */
	PageSupport<BankCodeDTO> queryBankCode(BankCodeQuery query);
}
