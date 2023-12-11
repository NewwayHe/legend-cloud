/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.bo.UserWalletBO;
import com.legendshop.pay.dto.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * (UserWallet)表服务接口
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
public interface UserWalletService {

	UserWalletDTO getByUserId(Long userId);

	/**
	 * 平台补偿
	 */
	R<Void> platformCompensation(String identifier, BigDecimal amount);

	/**
	 * 初始化用户钱包
	 */
	R<Void> initUserWallet(Integer offset, Integer size);

	/**
	 * 初始化用户钱包
	 */
	R<Void> initUserWallet(Long userId);

	/**
	 * 支付扣款
	 */
	@Deprecated
	R<Void> payDeduction(UserWalletDetailsDTO details);

	/**
	 * 支付扣款
	 */
	@Deprecated
	R<Void> payDeduction(List<UserWalletDetailsDTO> details);

	/**
	 * 用户钱包支付信息
	 */
	UserWalletPayDTO payInfo(Long userId);

	/**
	 * 用户发起提现
	 */
	@Deprecated
	R<Void> userWithdraw(UserWithdrawApplyDTO apply);

	/**
	 * 用户提现审核
	 */
	@Deprecated
	R<Void> userWithdrawAudit(Long id);

	@Deprecated
	R<Void> userWithdrawAudit(WithdrawalAuditDTO dtos);


	R<UserWalletBO> getCommissionByUserId(Long id);


	R<Void> passwordValidation(Long userId, String payPassword);

	@Deprecated
	R<Void> compensate(Long id);

}
