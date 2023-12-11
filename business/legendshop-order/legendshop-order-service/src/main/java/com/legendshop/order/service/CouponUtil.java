/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.SubmitOrderShopDTO;

import java.util.List;
import java.util.Map;

/**
 * 优惠券工具
 *
 * @author legendshop
 */
public interface CouponUtil {

	/**
	 * 构建平台优惠券查询
	 *
	 * @param shopOrderList
	 * @return
	 */
	Map<Long, ShopCouponDTO> buildPlatformCouponMap(List<SubmitOrderShopDTO> shopOrderList);

	/**
	 * 处理选择平台优惠券
	 *
	 * @param confirmOrderBo
	 * @param couponIds
	 * @return
	 */
	R<ConfirmOrderBO> handleSelectPlatformCoupon(ConfirmOrderBO confirmOrderBo, List<Long> couponIds);

	/**
	 * 选择商家券后置处理(重新计算平台券最优选择)
	 *
	 * @param confirmOrderBo
	 */
	R<ConfirmOrderBO> selectCouponPostProcess(ConfirmOrderBO confirmOrderBo);


	/**
	 * 商家优惠券分摊计算
	 *
	 * @param submitOrderShopDTO
	 * @return
	 */
	R<SubmitOrderShopDTO> shopCouponsShardCalculation(SubmitOrderShopDTO submitOrderShopDTO);

	/**
	 * 平台优惠券分摊计算
	 *
	 * @param ConfirmOrderBO
	 * @return
	 */
	R<ConfirmOrderBO> platformCouponsShardCalculation(ConfirmOrderBO ConfirmOrderBO, Map<Long, ShopCouponDTO> shopCouponMap);
}
