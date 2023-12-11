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
import com.legendshop.pay.bo.UserWalletBO;
import com.legendshop.pay.dto.UserWalletAmountDTO;
import com.legendshop.pay.entity.UserWallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * (UserWallet)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
public interface UserWalletDao extends GenericDao<UserWallet, Long> {

	@Deprecated
	int addition(Long id, BigDecimal amount);

	/**
	 * 退还冻结金额
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	@Deprecated
	int returnedFrozenAmount(Long userId, BigDecimal amount);

	/**
	 * 实扣金额
	 */
	@Deprecated
	int deductionAmount(Long userId, BigDecimal amount);

	@Deprecated
	int[] returnedFrozenAmount(List<UserWalletAmountDTO> dtoList);


	// --------------- 往上的方法需要删除

	List<UserWallet> queryByUserIds(List<Long> ids);

	UserWalletBO getCommissionByUserId(Long userId);

	/**
	 * 通过userId查询钱包
	 *
	 * @param userId
	 * @return
	 */
	UserWallet getByUserId(Long userId);

	/**
	 * 通过userId查询钱包
	 *
	 * @param userId
	 * @return
	 */
	List<UserWallet> getWalletByUserId(List<Long> userId);

	/**
	 * 减少已结算金额
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer reduceAvailableAmount(Long userId, BigDecimal amount);

	/**
	 * 增加已结算金额
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer addAvailableAmount(Long userId, BigDecimal amount);

	/**
	 * 扣减冻结金额
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer reduceFrozenAmount(Long userId, BigDecimal amount);

	/**
	 * 增加冻结金额
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer addFrozenAmount(Long userId, BigDecimal amount);

	/**
	 * 结算转冻结
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer frozenAmount(Long userId, BigDecimal amount);

	/**
	 * 冻结转结算
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	Integer returnFrozenAmount(Long userId, BigDecimal amount);

}
