/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.dao.YeepayDivideDao;
import com.legendshop.pay.entity.YeepayDivide;
import com.legendshop.pay.enums.YeepayDivideTypeEnum;
import org.springframework.stereotype.Repository;

/**
 * 易宝支付分账信息(YeepayDivide)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-31 20:51:36
 */
@Repository
public class YeepayDivideDaoImpl extends GenericDaoImpl<YeepayDivide, Long> implements YeepayDivideDao {

	@Override
	public YeepayDivide getByOrderId(Long orderId, Long yeepayOrderId, YeepayDivideTypeEnum typeEnum) {
		if (orderId == null) {
			return null;
		}

		LambdaEntityCriterion<YeepayDivide> criterion = new LambdaEntityCriterion<>(YeepayDivide.class);
		criterion.eq(YeepayDivide::getOrderId, orderId);
		criterion.eq(YeepayDivide::getType, typeEnum.getValue());
		return getByProperties(criterion);
	}

	@Override
	public void saveDivide(YeepayDivide yeepayDivide) {
		if (null == yeepayDivide.getId()) {
			this.save(yeepayDivide);
		} else {
			this.update(yeepayDivide);
		}
	}
}
