/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.admin;


import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponShopBO;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台优惠券(Coupon)表控制层
 *
 * @author legendshop
 * @since 2020-09-10 11:14:42
 */
@Tag(name = "优惠券管理")
@RestController
@RequestMapping(value = "/admin/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCouponController {

	/**
	 * 优惠券(Coupon)服务对象
	 */
	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponUserService couponUserService;

	/**
	 * 根据id查询优惠券
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "优惠券ID", required = true)
	@Operation(summary = "【平台】根据id查询优惠券", description = "")
	public R<CouponDTO> getById(@PathVariable("id") Long id) {
		return R.ok(couponService.getById(id));
	}

	/**
	 * 分页查询优惠券优惠店铺
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/shop")
	@Operation(summary = "【平台】分页查询优惠券优惠店铺", description = "")
	public R<PageSupport<CouponShopBO>> queryCouponShopPage(CouponQuery couponQuery) {
		if (ObjectUtil.isEmpty(couponQuery.getCouponId())) {
			return R.ok();
		}
		return R.ok(couponService.queryCouponShopPage(couponQuery));
	}

	/**
	 * 根据优惠券id查询已领取的用户列表
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/user")
	@Operation(summary = "【平台】根据优惠券id查询已领取的用户列表", description = "")
	public R<PageSupport<CouponUserBO>> queryCouponUserPage(CouponQuery couponQuery) {
		if (ObjectUtil.isEmpty(couponQuery.getCouponId())) {
			return R.fail("请选择优惠券");
		}
		couponQuery.setShopId(0L);
		return R.ok(couponUserService.queryPage(couponQuery));
	}

	/**
	 * 根据id删除优惠券
	 *
	 * @param ids
	 * @return
	 */
	@PutMapping("/updateStatus/{status}")
	@SystemLog("修改优惠券状态")
	@Parameter(name = "status", description = "优惠券ID", required = true)
	@Operation(summary = "【平台】修改优惠券状态", description = "")
	public R updateStatus(@RequestBody List<Long> ids, @PathVariable @EnumValid(target = CouponStatusEnum.class, message = "优惠券状态不匹配") Integer status) {
		return couponService.batchUpdateStatus(ids, status, 0L);
	}

	/**
	 * 保存
	 *
	 * @param couponDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存优惠券")
	@Operation(summary = "【平台】保存优惠券", description = "")
	public R save(@Valid @RequestBody CouponDTO couponDTO) {
		couponDTO.setShopProviderFlag(false);
		couponDTO.setShopId(0L);
		return couponService.save(couponDTO);
	}

	/**
	 * 优惠券分页查询
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【平台】优惠券分页查询", description = "")
	public R<PageSupport<CouponVO>> page(CouponQuery couponQuery) {
		couponQuery.setShopId(0L);
		return R.ok(couponService.page(couponQuery));
	}

}
