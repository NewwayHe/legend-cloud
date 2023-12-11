/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.UserWithdrawDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象的钱包处理表
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractWithdrawHandler implements WithdrawHandler {

	@Autowired
	protected SysParamsApi sysParamsApi;

	protected final ObjectMapper mapper = new ObjectMapper();

	@Override
	public R<Void> handler(Long withdrawSerialNo) {
		// 验证平台设置
		R<Void> checkResult = this.checkUserWithdrawSetting();
		if (!checkResult.success()) {
			return checkResult;
		}
		// 验证用户设置
		log.info("开始检验提现信息~");
		R<UserWithdrawDTO> userWalletResult = this.checkUserWallet(withdrawSerialNo);
		if (!userWalletResult.success()) {
			return R.fail(userWalletResult.getMsg());
		}
		// 发起提现
		log.info("开始发起提现~");
		R<Void> userWithdrawHandlerResult = this.userWithdrawHandler(userWalletResult.getData());
		if (!userWithdrawHandlerResult.success()) {
			return userWithdrawHandlerResult;
		}

		// 提现完成
		return this.completeWithdrawal(withdrawSerialNo);
	}

	/**
	 * 发起提现处理方法
	 */
	protected abstract R<Void> userWithdrawHandler(UserWithdrawDTO dto);

	/**
	 * 校验平台设置
	 */
	protected abstract R<Void> checkUserWithdrawSetting();

	/**
	 * 用户钱包提现金额冻结校验
	 */
	protected abstract R<UserWithdrawDTO> checkUserWallet(Long withdrawSerialNo);

	/**
	 * 完成用户提现记录
	 */
	protected abstract R<Void> completeWithdrawal(Long withdrawSerialNo);
}
