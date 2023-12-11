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
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品立减(无门槛)优惠券结算规则执行器
 *
 * @author legendshop
 */
@Component("INSTANT_REDUCTION")
@Slf4j
public class InstantReductionExecutor extends AbstractCouponExecutor {


	@Override
	public CouponRuleEnum couponRule() {
		return CouponRuleEnum.INSTANT_REDUCTION;
	}


	/**
	 * 处理优惠券的选择
	 *
	 * @param couponList
	 * @param productItems
	 * @param selectItem
	 */
	@Override
	public void processSelectCoupons(List<CouponItemDTO> couponList, List<ProductItemDTO> productItems, CouponItemDTO selectItem) {
		//用户选中的优惠券
		couponList.forEach(couponItemDTO -> {
			if (couponItemDTO.getCouponId().equals(selectItem.getCouponId())) {
				couponItemDTO.setSelectStatus(CouponSelectStatusEnum.SELECTED.getStatus());
			} else {
				couponItemDTO.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus());
			}
		});
	}

	@Override
	protected List<ProductItemDTO> filterProducts(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems) {
		return productItems;
	}


	/**
	 * 过滤不是自己的优惠券
	 *
	 * @param couponList
	 * @return
	 */
	@Override
	public List<CouponItemDTO> filter(List<CouponItemDTO> couponList) {
		return couponList.stream().filter(couponItemDTO -> couponItemDTO.getMinPoint().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
	}

}
