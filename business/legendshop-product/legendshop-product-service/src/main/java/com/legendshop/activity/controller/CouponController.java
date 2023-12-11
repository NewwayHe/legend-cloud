/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.service.CouponOrderService;
import com.legendshop.activity.service.CouponService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户优惠券管理")
public class CouponController {

	private final CouponService couponService;
	private final UserTokenUtil userTokenUtil;
	/**
	 * (CouponOrder)服务对象
	 */
	@Autowired
	private CouponOrderService couponOrderService;


	/**
	 * 用户可领取优惠券列表(店铺、skuId)
	 *
	 * @return
	 */
	@Operation(summary = "【用户】用户可领取优惠券列表(店铺、skuId)")
	@GetMapping("/receivable")
	public R<PageSupport<CouponDTO>> receivable(CouponQuery couponQuery, HttpServletRequest request) {
		Long userId = userTokenUtil.getUserId(request);
		if (userId != null && userId == 0L) {
			userId = null;
		}
		couponQuery.setUserId(userId);
		couponQuery.setReceiveType(CouponReceiveTypeEnum.FREE.getValue());
		//查询用户可领取优惠券列表
		return R.ok(couponService.receivablePage(couponQuery));
	}

	/**
	 * 用户可领取积分优惠券列表
	 *
	 * @return
	 */
	@Operation(summary = "【用户】用户可领取积分优惠券列表", description = "")
	@GetMapping("intergral/receivable")
	public R<PageSupport<CouponDTO>> intergralReceivable(CouponQuery couponQuery, HttpServletRequest request) {
		Long userId = userTokenUtil.getUserId(request);
		if (ObjectUtil.isNotEmpty(userId)) {
			couponQuery.setUserId(userId);
		}
		couponQuery.setReceiveType(CouponReceiveTypeEnum.INTEGRAL.getValue());
		//查询用户可领取优惠券列表
		return R.ok(couponService.receivablePage(couponQuery));
	}

	@PostMapping("/getById")
	@Operation(summary = "【用户】优惠券详情", description = "")
	public R<CouponDTO> getCouponById(@RequestParam Long id, HttpServletRequest request) {
		CouponDTO couponDTO = couponService.getById(id);
		Long userId = userTokenUtil.getUserId(request);
		if (userId != null && userId == 0L) {
			userId = null;
		}
		couponService.getIdenticalAvailableCouponUser(couponDTO, userId);
		if (ObjectUtil.isNotEmpty(couponDTO) && ObjectUtil.isNotEmpty(userId)) {
			List<CouponUserDTO> couponUsers = couponService.queryUnusedCouponIdByUserId(userId);
			Map<Long, List<CouponUserDTO>> listMap = couponUsers.stream().collect(Collectors.groupingBy(CouponUserDTO::getCouponId));
			if (CollUtil.isNotEmpty(couponUsers) && listMap.containsKey(couponDTO.getId())) {
				couponDTO.setReceivedFlag(true);
				CouponUserDTO couponUser = listMap.get(couponDTO.getId()).get(0);
				couponDTO.setUseStartTime(couponUser.getUseStartTime());
				couponDTO.setUseEndTime(couponUser.getUseEndTime());
			}

			//发送活动访问记录MQ
			couponService.sendActivityViewMQ(couponDTO, userId);
		}
		return R.ok(couponDTO);
	}

	@PostMapping("/listReceivable")
	public R<List<CouponDTO>> listReceivable(@RequestBody CouponQuery couponQuery) {
		couponQuery.setReceiveType(CouponReceiveTypeEnum.FREE.getValue());
		return R.ok(couponService.listReceivable(couponQuery));
	}

	@PostMapping("/handleSelectCoupons")
	public R<ShopCouponDTO> handleSelectCoupons(@RequestBody ShopCouponDTO shopCouponDTO) {
		return R.ok(couponService.handleSelectCoupons(shopCouponDTO));
	}

	/**
	 * @param userId
	 * @param shopCouponDTOMap
	 * @return
	 */
	@PostMapping("/getShopBestCoupons")
	public R<Map<Long, ShopCouponDTO>> getShopBestCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		return R.ok(couponService.getShopBestCoupons(userId, shopCouponDTOMap));
	}

	@PostMapping("/getBestPlatFormCoupons")
	public R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestParam(value = "userId") Long userId, @RequestBody Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		return R.ok(couponService.getBestPlatFormCoupons(userId, shopCouponDTOMap));
	}

	@PostMapping("/getBestPlatFormCouponsByPlatformCoupons")
	public R<List<CouponItemDTO>> getBestPlatFormCoupons(@RequestBody PlatFormCouponDTO platFormCouponDTO) {
		return R.ok(couponService.getBestPlatFormCoupons(platFormCouponDTO.getPlatformCoupons(), platFormCouponDTO.getShopCouponDTOMap()));
	}

	@PostMapping("/batchUpdateStatus")
	public R<Void> batchUpdateUsedStatus(@RequestBody List<CouponOrderDTO> couponOrderList) {
		return couponService.batchUpdateUsedStatus(couponOrderList);
	}

	@PostMapping("/handlerShopCouponsShard")
	public R<ShopCouponDTO> handlerShopCouponsShard(@RequestBody ShopCouponDTO shopCouponDTO) {
		return R.ok(couponService.handlerShopCouponsShard(shopCouponDTO));
	}

	@PostMapping("/filterShop")
	public R<List<Long>> filterShop(@RequestBody CouponItemDTO selectItem) {
		return R.ok(couponService.filterShop(selectItem.getCouponId()));
	}

	@PostMapping("/handleSelectPlatformCoupons")
	public R<List<CouponItemDTO>> handleSelectPlatformCoupons(@RequestBody SelectPlatformCouponDTO selectPlatformCouponDTO) {
		return R.ok(couponService.handleSelectPlatformCoupons(selectPlatformCouponDTO));
	}

	@GetMapping("/subUseCount")
	public void subUseCount(@RequestParam("couponIds") List<Long> couponIds) {
		couponService.subUseCount(couponIds);
	}

	@PostMapping("/queryCouponsByStatus")
	public R<PageSupport<CouponDTO>> queryCouponsByStatus(@RequestBody CouponQuery couponQuery) {
		return R.ok(couponService.queryCouponsByStatus(couponQuery));
	}

	@GetMapping("/getCouponsById")
	public R<CouponDTO> getCouponsById(@RequestParam("couponId") Long couponId) {
		return R.ok(couponService.getCouponsById(couponId));
	}

}
