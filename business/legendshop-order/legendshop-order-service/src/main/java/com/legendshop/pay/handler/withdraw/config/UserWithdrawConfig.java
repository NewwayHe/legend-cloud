/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw.config;


import com.legendshop.pay.enums.WithdrawTypeEnum;
import com.legendshop.pay.handler.withdraw.WithdrawHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现策略配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
public class UserWithdrawConfig {

	@Autowired
	@Qualifier("ALI_USER_WALLET")
	private WithdrawHandler userAliWithdrawHandler;

	@Autowired
	@Qualifier("WECHAT_USER_WALLET")
	private WithdrawHandler userWeChatWithdrawHandler;


	@Bean(name = "withdrawBusinessMap")
	public Map<String, WithdrawHandler> withdrawBusinessMap() {
		Map<String, WithdrawHandler> strategyMap = new HashMap<>(16);
		strategyMap.put(WithdrawTypeEnum.WECHAT_WITHDRAW_WALLET.getValue(), userWeChatWithdrawHandler);
		strategyMap.put(WithdrawTypeEnum.ALI_WITHDRAW_WALLET.getValue(), userAliWithdrawHandler);
		log.info("初始化提现实现上下文: {}", strategyMap);
		return strategyMap;
	}

}
