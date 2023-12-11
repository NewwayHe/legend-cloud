/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.data.dao.CouponViewDao;
import com.legendshop.data.entity.CouponView;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * (CouponView)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-06 11:49:51
 */
@Repository
public class CouponViewDaoImpl extends GenericDaoImpl<CouponView, Long> implements CouponViewDao {

	@Override
	public CouponView queryVisit(Long couponId, String source, Date createTime) {
		LambdaEntityCriterion<CouponView> criterion = new LambdaEntityCriterion<>(CouponView.class, true);
		criterion.eq(CouponView::getCouponId, couponId)
				.eq(CouponView::getSource, source)
				.eq(CouponView::getCreateTime, createTime);
		return getByProperties(criterion);
	}


}
