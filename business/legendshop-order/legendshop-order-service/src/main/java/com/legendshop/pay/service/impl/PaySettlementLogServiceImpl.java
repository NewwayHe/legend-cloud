/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.pay.dao.PaySettlementLogDao;
import com.legendshop.pay.dto.PaySettlementLogDTO;
import com.legendshop.pay.service.PaySettlementLogService;
import com.legendshop.pay.service.convert.PaySettlementLogConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 支付单日志表【主要记录支付单异常记录，协助分析用户支付行为，以防支异常或攻击】(PaySettlementLog)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-28 11:48:12
 */
@Service
@RequiredArgsConstructor
public class PaySettlementLogServiceImpl implements PaySettlementLogService {

	final private PaySettlementLogDao paySettlementLogDao;
	final private PaySettlementLogConverter converter;

	@Override
	public void saveLog(PaySettlementLogDTO paySettlementLogDTO) {
		paySettlementLogDao.save(converter.from(paySettlementLogDTO));
	}
}
