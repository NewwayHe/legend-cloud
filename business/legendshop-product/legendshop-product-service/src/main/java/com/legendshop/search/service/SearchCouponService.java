/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import com.legendshop.search.dto.CouponDocumentDTO;

import java.util.List;

/**
 * @author legendshop
 */
public interface SearchCouponService {
	/**
	 * 更具SKUIDS 查找可用的优惠券索引
	 *
	 * @param skuIds 商品SKU Ids
	 * @param shopId 店铺
	 * @return
	 */
	List<CouponDocumentDTO> queryCouponBySkuIdsAndShopId(List<Long> skuIds, Long shopId);

	/**
	 * 更具使用类型和 店铺标识来查找优惠券 共六中查询
	 *
	 * @param skuIds           当前skuId列表可用的优惠券
	 * @param shopIds          当前店铺列表可用的优惠券
	 * @param shopProviderFlag true 商家 false 平台
	 * @return
	 */
	List<CouponDocumentDTO> queryByUserTypeAndShopProviderFlag(List<Long> skuIds, List<Long> shopIds, Boolean shopProviderFlag);
}
