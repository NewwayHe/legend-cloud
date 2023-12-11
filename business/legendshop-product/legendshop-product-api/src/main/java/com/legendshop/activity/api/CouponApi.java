/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@FeignClient(contextId = "couponApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface CouponApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 查询用户可领取优惠券列表
	 *
	 * @param couponQuery
	 * @return
	 */
	@PostMapping(PREFIX + "/coupon/listReceivable")
	R<List<CouponDTO>> listReceivable(@RequestBody CouponQuery couponQuery);


	/**
	 * 处理优惠券
	 *
	 * @param shopCouponDTO
	 */
	@PostMapping(PREFIX + "/coupon/handleSelectCoupons")
	R<ShopCouponDTO> handleSelectCoupons(@RequestBody ShopCouponDTO shopCouponDTO);


	/**
	 * 下单获取优惠券列表,获取用户最优优惠券组合
	 *
	 * @param userId
	 * @param shopCouponMap
	 * @return
	 */
	@PostMapping(PREFIX + "/coupon/getShopBestCoupons")
	R<Map<Long, ShopCouponDTO>> getShopBestCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponMap);


	/**
	 * 获取最优的平台优惠券
	 *
	 * @param userId
	 */
	@PostMapping(PREFIX + "/coupon/getBestPlatFormCoupons")
	R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponDTOMap);

	/**
	 * 获取最优的平台优惠券
	 *
	 * @param platFormCouponDTO
	 */
	@PostMapping(PREFIX + "/coupon/getBestPlatFormCouponsByPlatformCoupons")
	R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestBody PlatFormCouponDTO platFormCouponDTO);

	/**
	 * 批量更新插入订单优惠券使用明细数据
	 *
	 * @param couponOrderList
	 * @return
	 */
	@PostMapping(PREFIX + "/coupon/batchUpdateStatus")
	R<Void> batchUpdateUsedStatus(@RequestBody List<CouponOrderDTO> couponOrderList);

	/**
	 * 处理优惠券的分摊金额
	 *
	 * @param shopCouponDTO
	 */
	@PostMapping(PREFIX + "/coupon/handlerShopCouponsShard")
	R<ShopCouponDTO> handlerShopCouponsShard(@RequestBody ShopCouponDTO shopCouponDTO);

	/**
	 * 处理平台优惠券非通用券，过滤商家
	 */
	@PostMapping(PREFIX + "/coupon/filterShop")
	R<List<Long>> filterShop(@RequestBody CouponItemDTO selectItem);

	/**
	 * 处理选择平台优惠券
	 *
	 * @param selectPlatformCouponDTO
	 * @return
	 */
	@PostMapping(PREFIX + "/coupon/handleSelectPlatformCoupons")
	R<List<CouponItemDTO>> handleSelectPlatformCoupons(@RequestBody SelectPlatformCouponDTO selectPlatformCouponDTO);


	@GetMapping(PREFIX + "/coupon/subUseCount")
	void subUseCount(@RequestParam("couponIds") List<Long> couponIds);


	/**
	 * 根据参数查找可领取优惠卷加入ES索引
	 *
	 * @param
	 * @return
	 */
	@PostMapping(PREFIX + "/coupon/queryCouponsByStatus")
	R<PageSupport<CouponDTO>> queryCouponsByStatus(@RequestBody CouponQuery couponQuery);


	/**
	 * 更具优惠卷ID查找优惠卷
	 *
	 * @param couponId
	 * @return
	 */
	@GetMapping(PREFIX + "/coupon/getCouponsById")
	R<CouponDTO> getCouponsById(@RequestParam("couponId") Long couponId);

	/**
	 * 优惠券到点上线定时任务处理
	 */
	@GetMapping(PREFIX + "/couponOnLineJobHandle")
	R<Void> couponOnLineJobHandle();

	/**
	 * 优惠券到点下线 定时任务处理
	 */
	@GetMapping(PREFIX + "/couponOffLineJobHandle")
	R<Void> couponOffLineJobHandle();

	/**
	 * 用户优惠券到点上线 定时任务处理
	 */
	@GetMapping(PREFIX + "/userCouponValidJobHandle")
	R<Void> userCouponValidJobHandle();

	/**
	 * 用户优惠券到点下线 定时任务处理
	 */
	@GetMapping(PREFIX + "/userCouponInvalidJobHandle")
	R<Void> userCouponInvalidJobHandle();
}
