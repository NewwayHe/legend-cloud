/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.api;

import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.dto.OrderRefundCouponDTO;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
public class CouponUserApiImpl implements CouponUserApi {

	private final CouponUserService couponUserService;
	private final CouponService couponService;


	@Override
	public R<Integer> userCouponCount(@RequestParam(value = "userId") Long userId) {
		return couponUserService.userCouponCount(userId);
	}


	@Override
	public R<List<CouponUserDTO>> getByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber) {
		return R.ok(couponUserService.getByOrderNumber(orderNumber));
	}

	@Override
	public R<Void> update(@RequestBody List<CouponUserDTO> couponUsers) {
		couponUserService.update(couponUsers);
		return R.ok();
	}

	@Override
	public R<Void> updateOrder(@RequestBody List<OrderRefundCouponDTO> couponUsers) {
		couponUserService.updateCouponOrder(couponUsers);
		couponService.updateUseCount(couponUsers);
		return R.ok();
	}

}
