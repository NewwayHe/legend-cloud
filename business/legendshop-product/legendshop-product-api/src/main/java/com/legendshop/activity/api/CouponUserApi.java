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
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "couponUserApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface CouponUserApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 获取用户优惠券数量的方法。
	 *
	 * @param userId 用户 ID
	 * @return 用户优惠券数量
	 */
	@GetMapping(PREFIX + "/p/coupon/userCouponCount")
	R<Integer> userCouponCount(@RequestParam(value = "userId") Long userId);


	/**
	 * 根据订单编号获取优惠券信息的方法。
	 *
	 * @param orderNumber 订单编号
	 * @return 包含优惠券用户信息的列表
	 */
	@GetMapping(PREFIX + "/p/coupon/getByOrderNumber")
	R<List<CouponUserDTO>> getByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);


	/**
	 * 更新
	 *
	 * @param couponUsers
	 * @return
	 */
	@PutMapping(PREFIX + "/p/coupon/update")
	R<Void> update(@RequestBody List<CouponUserDTO> couponUsers);

	/**
	 * 退款更新优惠券状态和使用数量
	 *
	 * @param couponUsers
	 * @return
	 */
	@PostMapping(PREFIX + "/p/coupon/updateOrder")
	R<Void> updateOrder(@RequestBody List<OrderRefundCouponDTO> couponUsers);


}
