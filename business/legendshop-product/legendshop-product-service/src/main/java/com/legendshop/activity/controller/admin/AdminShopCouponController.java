/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.admin;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.activity.vo.CouponVO;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.product.bo.SkuBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台查看商家优惠券(Coupon)表控制层
 *
 * @author legendshop
 * @since 2020-09-10 11:14:42
 */
@Tag(name = "平台查看商家优惠券")
@RestController
@RequestMapping(value = "/admin/shop/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminShopCouponController {

	/**
	 * 优惠券(Coupon)服务对象
	 */
	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponUserService couponUserService;

	/**
	 * 根据id查询商家优惠券
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_activity_shop_coupon_get')")
	@Parameter(name = "id", description = "优惠券ID", required = true)
	@Operation(summary = "【平台】根据id查询商家优惠券", description = "")
	public R<CouponDTO> getById(@PathVariable("id") Long id) {
		CouponDTO couponDTO = couponService.getById(id);
		return R.ok(couponDTO);
	}

	/**
	 * 根据id查询商家优惠券商品
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/product")
	@PreAuthorize("@pms.hasPermission('admin_activity_shop_coupon_queryCouponProductPage')")
	@Operation(summary = "【平台】根据id查询商家优惠券优惠商品", description = "")
	public R<PageSupport<SkuBO>> queryCouponProductPage(CouponQuery couponQuery) {
		couponQuery.setShopProviderFlag(true);
		return R.ok(couponService.queryCouponProductPage(couponQuery));
	}

	/**
	 * 根据商家优惠券id查询已领取的用户列表
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/user")
	@PreAuthorize("@pms.hasPermission('admin_activity_shop_coupon_queryCouponUserPage')")
	@Operation(summary = "【平台】根据商家优惠券id查询已领取的用户列表", description = "")
	public R<PageSupport<CouponUserBO>> queryCouponUserPage(CouponQuery couponQuery) {
		return R.ok(couponUserService.queryPage(couponQuery));
	}

	/**
	 * 根据id删除商家优惠券
	 *
	 * @param ids
	 * @return
	 */
	@PutMapping("/updateStatus/{status}")
	@SystemLog("修改商家优惠券状态")
	@Parameter(name = "status", description = "优惠券ID", required = true)
	@PreAuthorize("@pms.hasPermission('admin_activity_shop_coupon_updateStatus')")
	@Operation(summary = "【平台】修改商家优惠券状态", description = "")
	public R updateStatus(@RequestBody List<Long> ids, @PathVariable @EnumValid(target = CouponStatusEnum.class, message = "优惠券状态不匹配") Integer status) {
		return couponService.batchUpdateStatus(ids, status, null);
	}

	/**
	 * 商家优惠券分页查询
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_activity_shop_coupon_page')")
	@Operation(summary = "【平台】商家优惠券分页查询", description = "")
	public R<PageSupport<CouponVO>> page(CouponQuery couponQuery) {
		couponQuery.setShopProviderFlag(true);
		couponQuery.setIsPlatform(true);
		return R.ok(couponService.page(couponQuery));
	}

}
