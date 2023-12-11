/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 钱包业务策略上下文
 *
 * @author legendshop
 */
@Slf4j
@Component
public class WithdrawContext {

	@Autowired
	@Qualifier("withdrawBusinessMap")
	private Map<String, WithdrawHandler> strategyMap;

	public R<Void> executeWithdraw(String withdrawType, Long withdrawSerialNo) {
		log.info("发起提现，withdrawType: {}, withdrawSerialNo: {}, strategyMap: {}", withdrawType, withdrawSerialNo, strategyMap);
		WithdrawHandler strategy = strategyMap.get(withdrawType);
		if (null == strategy) {
			throw new BusinessException("未找到对应的钱包处理类！");
		}
		return strategy.handler(withdrawSerialNo);
	}


}
