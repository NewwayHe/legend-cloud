/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.coupon.impl;

import com.legendshop.activity.coupon.AbstractCouponExecutor;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.enums.CouponRuleEnum;
import com.legendshop.product.dto.ProductItemDTO;

import java.util.List;

/**
 * 规则执行器抽象类
 *
 * @author legendshop
 */
public class CouponExecutor extends AbstractCouponExecutor {


	@Override
	protected List<ProductItemDTO> filterProducts(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems) {
		return null;
	}

	@Override
	protected void processSelectCoupons(List<CouponItemDTO> couponList, List<ProductItemDTO> productItems, CouponItemDTO couponItemDTO) {

	}

	@Override
	public CouponRuleEnum couponRule() {
		return null;
	}

	@Override
	public List<CouponItemDTO> filter(List<CouponItemDTO> couponItems) {
		return null;
	}
}
