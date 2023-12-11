/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.bo.UserWalletBO;
import com.legendshop.pay.dao.UserWalletDao;
import com.legendshop.pay.dto.UserWalletAmountDTO;
import com.legendshop.pay.entity.UserWallet;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * (UserWallet)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
@Repository
public class UserWalletDaoImpl extends GenericDaoImpl<UserWallet, Long> implements UserWalletDao {

	@Override
	public int addition(Long id, BigDecimal amount) {
		return super.update("UPDATE ls_user_wallet SET available_amount = available_amount + ? , cumulative_amount = cumulative_amount + ? WHERE user_id = ?", amount, amount, id);
	}

	@Override
	public List<UserWallet> queryByUserIds(List<Long> ids) {
		return super.queryByProperties(new EntityCriterion().in("userId", ids.toArray()));
	}

	@Override
	public UserWallet getByUserId(Long userId) {
		return super.getByProperties(new EntityCriterion().eq("userId", userId));
	}

	@Override
	public List<UserWallet> getWalletByUserId(List<Long> userId) {
		if (CollUtil.isEmpty(userId)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<UserWallet> criterion = new LambdaEntityCriterion<>(UserWallet.class);
		criterion.in(UserWallet::getUserId, userId);
		List<UserWallet> userWallets = queryDTOByProperties(criterion);
		if (CollectionUtils.isEmpty(userWallets)) {
			return Collections.emptyList();
		}
		return userWallets;
	}

	@Override
	public int returnedFrozenAmount(Long userId, BigDecimal amount) {
		return super.update("UPDATE ls_user_wallet SET available_amount = available_amount + ?, frozen_amount = frozen_amount - ? WHERE user_id = ? AND frozen_amount >= ?", amount, amount, userId, amount);
	}

	@Override
	public int deductionAmount(Long userId, BigDecimal amount) {
		return super.update("UPDATE ls_user_wallet SET frozen_amount = frozen_amount - ?, cumulative_expenditure = cumulative_expenditure + ? WHERE frozen_amount >= ? AND user_id = ?", amount, amount, amount, userId);
	}

	@Override
	public UserWalletBO getCommissionByUserId(Long userId) {
		return get("select cumulative_expenditure , available_amount , frozen_amount from ls_user_wallet where user_id = ? ", UserWalletBO.class, userId);
	}

	@Override
	public int[] returnedFrozenAmount(List<UserWalletAmountDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return new int[]{};
		}
		List<Object[]> params = new ArrayList<>(dtoList.size());
		dtoList.forEach(e -> params.add(new Object[]{e.getAmount(), e.getAmount(), e.getUserId(), e.getAmount()}));
		return super.batchUpdate("UPDATE ls_user_wallet SET available_amount = available_amount + ?, frozen_amount = frozen_amount - ? WHERE user_id = ? AND frozen_amount >= ?", params);
	}


	@Override
	public Integer addAvailableAmount(Long userId, BigDecimal amount) {
		return super.update("UPDATE ls_user_wallet SET available_amount = available_amount + ? , cumulative_amount = cumulative_amount + ?" +
				" WHERE user_id = ?", amount, amount, userId);
	}

	@Override
	public Integer reduceAvailableAmount(Long userId, BigDecimal amount) {
		return update("update ls_user_wallet set available_amount = available_amount - ? , cumulative_expenditure = cumulative_expenditure + ?, " +
				"frozen_amount = frozen_amount + ? where user_id = ? and available_amount >= 0", amount, amount, amount, userId);
	}

	@Override
	public Integer frozenAmount(Long userId, BigDecimal amount) {
		return super.update("update ls_user_wallet set available_amount = available_amount - ? ,frozen_amount = frozen_amount + ?, " +
				"cumulative_expenditure = cumulative_expenditure + ?, update_time = ? where user_id = ? and available_amount - ? >= 0", amount, amount, amount, DateUtil.date(), userId, amount);
	}

	@Override
	public Integer reduceFrozenAmount(Long userId, BigDecimal amount) {
		return super.update("update ls_user_wallet set frozen_amount = frozen_amount - ?, update_time = ? where user_id = ?  and  frozen_amount - ? >= 0", amount, DateUtil.date(), userId, amount);
	}

	@Override
	public Integer addFrozenAmount(Long userId, BigDecimal amount) {
		return super.update("update ls_user_wallet set frozen_amount = frozen_amount + ?, update_time = ? where user_id = ?", amount, DateUtil.date(), userId);
	}

	@Override
	public Integer returnFrozenAmount(Long userId, BigDecimal amount) {
		return super.update("update ls_user_wallet set frozen_amount = frozen_amount - ?,  available_amount = available_amount + ?, cumulative_amount = cumulative_amount + ?,update_time = ? where user_id = ? and frozen_amount - ? >= 0 ", amount, amount, amount, DateUtil.date(), userId, amount);
	}
}
