/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.shop;

import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/p/couponUser", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户优惠券管理")
public class ShopCouponUserController {


	private final CouponUserService couponUserService;

	/**
	 * 根据id删除优惠券
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("/updateStatus")
	@SystemLog("强制失效用户优惠券状态")
	@Parameters({
			@Parameter(name = "id", description = "优惠券ID", required = true)
	})
	@PreAuthorize("@pms.hasPermission('s_activity_couponUser_updateStatus')")
	@Operation(summary = "【商家】强制失效用户优惠券状态", description = "")
	public R updateStatus(@RequestParam Long id) {
		return couponUserService.updateStatus(id, CouponUserStatusEnum.INVALID.getValue(), SecurityUtils.getShopUser().getShopId());
	}
}
