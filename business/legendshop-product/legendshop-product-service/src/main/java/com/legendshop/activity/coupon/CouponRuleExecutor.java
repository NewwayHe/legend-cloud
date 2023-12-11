/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.coupon;

import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.enums.CouponRuleEnum;
import com.legendshop.product.dto.ProductItemDTO;

import java.util.List;

/**
 * 优惠券执行器
 *
 * @author legendshop
 */
public interface CouponRuleExecutor {


	/**
	 * 获取优惠券的计算规则
	 *
	 * @return
	 */
	CouponRuleEnum couponRule();


	/**
	 * 过滤
	 *
	 * @param couponItems
	 * @return
	 */
	List<CouponItemDTO> filter(List<CouponItemDTO> couponItems);

	/**
	 * 计算选择优惠券
	 * @param couponList
	 * @param productItems
	 * @param couponItemDTO
	 */
	void handleSelectCoupons(List<CouponItemDTO> couponList, List<ProductItemDTO> productItems, CouponItemDTO couponItemDTO);

	/**
	 * 处理优惠券分摊
	 *
	 * @param selectCouponItem
	 * @param productItems
	 */
	void handlerCouponsShard(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems);

}
