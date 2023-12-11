/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.dao.UserWalletDetailedCentreDao;
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.entity.UserWalletDetailedCentre;
import com.legendshop.pay.enums.WalletCentreStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-27 13:56:51
 */
@Repository
public class UserWalletDetailedCentreDaoImpl extends GenericDaoImpl<UserWalletDetailedCentre, Long> implements UserWalletDetailedCentreDao {

	@Override
	public Integer updateStatus(Long id, Integer oldStatus, Integer newStatus) {
		return update("update ls_user_wallet_detailed_centre set status = ?, update_time = ? where id = ? and status = ?", newStatus, DateUtil.date(), id, oldStatus);
	}

	@Override
	public List<UserWalletDetailedCentreDTO> queryByStatus(WalletCentreStatusEnum status) {
		if (status == null) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<UserWalletDetailedCentreDTO> criterion = new LambdaEntityCriterion<>(UserWalletDetailedCentreDTO.class);
		criterion.eq(UserWalletDetailedCentreDTO::getStatus, status.getValue());
		return queryDTOByProperties(criterion);
	}
}
