/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.activity.dao.CouponOrderDao;
import com.legendshop.activity.entity.CouponOrder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * (CouponOrder)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
@Repository
public class CouponOrderDaoImpl extends GenericDaoImpl<CouponOrder, Long> implements CouponOrderDao {

	@Override
	public List<CouponOrder> queryByUserCouponId(List<Long> userCouponIds) {
		if (CollUtil.isEmpty(userCouponIds)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<CouponOrder> criterion = new LambdaEntityCriterion<>(CouponOrder.class);
		criterion.in(CouponOrder::getUserCouponId, userCouponIds);

		return queryByProperties(criterion);
	}
}
