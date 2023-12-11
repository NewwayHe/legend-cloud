/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.admin;

import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/admin/couponUser", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户优惠券管理")
public class AdminCouponUserController {

	private final CouponUserService couponUserService;

	/**
	 * 根据id删除优惠券
	 * 用户优惠券管理
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("/updateStatus")
	@SystemLog("强制失效用户优惠券状态")
	@Parameters({
			@Parameter(name = "id", description = "优惠券ID", required = true)
	})
	@Operation(summary = "【平台】强制失效用户优惠券状态", description = "")
	public R updateStatus(@RequestParam Long id) {
		return couponUserService.updateStatus(id, CouponUserStatusEnum.INVALID.getValue(), null);
	}
}
