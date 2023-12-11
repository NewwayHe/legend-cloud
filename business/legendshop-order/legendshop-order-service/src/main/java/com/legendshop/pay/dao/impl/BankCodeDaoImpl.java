/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.pay.dao.BankCodeDao;
import com.legendshop.pay.dto.BankCodeDTO;
import com.legendshop.pay.entity.BankCode;
import com.legendshop.pay.query.BankCodeQuery;
import org.springframework.stereotype.Repository;

/**
 * 银行编码(BankCode)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-04-07 09:56:29
 */
@Repository
public class BankCodeDaoImpl extends GenericDaoImpl<BankCode, Long> implements BankCodeDao {

	@Override
	public PageSupport<BankCodeDTO> queryBankCode(BankCodeQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.like("name", query.getName(), MatchMode.ANYWHERE);
		queryMap.put("code", query.getCode());

		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(BankCodeDTO.class, query);
		sqlQuery.setSqlAndParameter("BankCode.queryBankCode", queryMap);

		return this.querySimplePage(sqlQuery);
	}
}
