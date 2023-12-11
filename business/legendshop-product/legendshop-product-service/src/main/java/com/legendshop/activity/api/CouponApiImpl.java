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
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
public class CouponApiImpl implements CouponApi {

	private final CouponService couponService;
	private final CouponUserService couponUserService;

	@Override
	public R<List<CouponDTO>> listReceivable(@RequestBody CouponQuery couponQuery) {
		couponQuery.setReceiveType(CouponReceiveTypeEnum.FREE.getValue());
		return R.ok(couponService.listReceivable(couponQuery));
	}

	@Override
	public R<ShopCouponDTO> handleSelectCoupons(@RequestBody ShopCouponDTO shopCouponDTO) {
		return R.ok(couponService.handleSelectCoupons(shopCouponDTO));
	}

	/**
	 * @param userId
	 * @param shopCouponDTOMap
	 * @return
	 */
	@Override
	public R<Map<Long, ShopCouponDTO>> getShopBestCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		return R.ok(couponService.getShopBestCoupons(userId, shopCouponDTOMap));
	}

	@Override
	public R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		return R.ok(couponService.getBestPlatFormCoupons(userId, shopCouponDTOMap));
	}

	@Override
	public R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestBody PlatFormCouponDTO platFormCouponDTO) {
		return R.ok(couponService.getBestPlatFormCoupons(platFormCouponDTO.getPlatformCoupons(), platFormCouponDTO.getShopCouponDTOMap()));
	}

	@Override
	public R<Void> batchUpdateUsedStatus(@RequestBody List<CouponOrderDTO> couponOrderList) {
		return couponService.batchUpdateUsedStatus(couponOrderList);
	}

	@Override
	public R<ShopCouponDTO> handlerShopCouponsShard(@RequestBody ShopCouponDTO shopCouponDTO) {
		return R.ok(couponService.handlerShopCouponsShard(shopCouponDTO));
	}

	@Override
	public R<List<Long>> filterShop(@RequestBody CouponItemDTO selectItem) {
		return R.ok(couponService.filterShop(selectItem.getCouponId()));
	}

	@Override
	public R<List<CouponItemDTO>> handleSelectPlatformCoupons(@RequestBody SelectPlatformCouponDTO selectPlatformCouponDTO) {
		return R.ok(couponService.handleSelectPlatformCoupons(selectPlatformCouponDTO));
	}

	@Override
	public void subUseCount(@RequestParam("couponIds") List<Long> couponIds) {
		couponService.subUseCount(couponIds);
	}

	@Override
	public R<PageSupport<CouponDTO>> queryCouponsByStatus(@RequestBody CouponQuery couponQuery) {
		return R.ok(couponService.queryCouponsByStatus(couponQuery));
	}

	@Override
	public R<CouponDTO> getCouponsById(@RequestParam("couponId") Long couponId) {
		return R.ok(couponService.getCouponsById(couponId));
	}

	@Override
	public R<Void> couponOnLineJobHandle() {
		return couponService.couponOnLineJobHandle();
	}

	@Override
	public R<Void> couponOffLineJobHandle() {
		return couponService.couponOffLineJobHandle();
	}

	@Override
	public R<Void> userCouponValidJobHandle() {
		return couponUserService.userCouponValidJobHandle();
	}

	@Override
	public R<Void> userCouponInvalidJobHandle() {
		return couponUserService.userCouponInvalidJobHandle();
	}
}
