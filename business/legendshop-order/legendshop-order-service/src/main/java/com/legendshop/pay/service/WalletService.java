/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
public interface WalletService {

	/**
	 * 冻结金额(减少已结算金额, 增加冻结金额)
	 *
	 * @param userId
	 * @param commission
	 * @return
	 */
	Integer frozenSettledCommission(Long userId, BigDecimal commission);


	/**
	 * 减少冻结金额
	 *
	 * @param userId
	 * @param commission
	 * @return
	 */
	Integer reduceFrozenCommission(Long userId, BigDecimal commission);

	/**
	 * 增加冻结金额
	 *
	 * @param userId
	 * @param commission
	 * @return
	 */
	Integer addFrozenCommission(Long userId, BigDecimal commission);

	/**
	 * 查询冻结金额
	 *
	 * @param userId
	 * @return
	 */
	Integer getFrozenCommission(Long userId);

	/**
	 * 查询已结算金额
	 *
	 * @param userId
	 * @return
	 */
	Integer getCommission(Long userId);

	/**
	 * 获取用户钱包
	 */
	Object getWallet(Long userId);

	/**
	 * 添加可用金额
	 *
	 * @return
	 */
	Integer addCommission(Long userId, BigDecimal commission);
}
