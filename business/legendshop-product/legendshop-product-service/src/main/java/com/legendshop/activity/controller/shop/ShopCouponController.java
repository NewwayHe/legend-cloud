/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.activity.vo.CouponVO;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券(Coupon)表控制层
 *
 * @author legendshop
 * @since 2020-09-10 11:14:42
 */
@Tag(name = "优惠券管理")
@RestController
@RequestMapping(value = "/s/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopCouponController {

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
	@PreAuthorize("@pms.hasPermission('s_product_coupon_get')")
	@Parameter(name = "id", description = "优惠券ID", required = true)
	@Operation(summary = "【商家】根据id查询优惠券", description = "")
	public R<CouponDTO> getById(@PathVariable("id") Long id) {
		CouponDTO couponDTO = couponService.getById(id);
		return R.ok(couponDTO);
	}

	/**
	 * 根据id查询优惠券商品
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/product")
	@Operation(summary = "【商家】根据id查询优惠券优惠商品", description = "")
	public R<PageSupport<SkuBO>> queryCouponProductPage(CouponQuery couponQuery) {
		couponQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(couponService.queryCouponProductPage(couponQuery));
	}

	/**
	 * 根据优惠券id查询已领取的用户列表
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/user")
	@PreAuthorize("@pms.hasPermission('s_product_coupon_queryCouponUserPage')")
	@Operation(summary = "【商家】根据优惠券id查询已领取的用户列表", description = "")
	public R<PageSupport<CouponUserBO>> queryCouponUserPage(CouponQuery couponQuery) {
		couponQuery.setShopId(SecurityUtils.getShopUser().getShopId());
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
	@PreAuthorize("@pms.hasPermission('s_product_coupon_updateStatus')")
	@Operation(summary = "【商家】修改优惠券状态", description = "")
	public R updateStatus(@RequestBody List<Long> ids, @PathVariable @EnumValid(target = CouponStatusEnum.class, message = "优惠券状态不匹配")
			Integer status) {
		return couponService.batchUpdateStatus(ids, status, SecurityUtils.getShopUser().getShopId());
	}

	/**
	 * 保存
	 *
	 * @param couponDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存优惠券")
	@PreAuthorize("@pms.hasPermission('s_product_coupon_save')")
	@Operation(summary = "【商家】保存优惠券", description = "")
	public R save(@Valid @RequestBody CouponDTO couponDTO) {
		couponDTO.setShopProviderFlag(true);
		couponDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		return couponService.save(couponDTO);
	}

	/**
	 * 优惠券分页查询
	 *
	 * @param couponQuery
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('s_product_coupon_page')")
	@Operation(summary = "【商家】优惠券分页查询", description = "")
	public R<PageSupport<CouponVO>> page(CouponQuery couponQuery) {
		couponQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		couponQuery.setIsPlatform(false);
		return R.ok(couponService.page(couponQuery));
	}

	/**
	 * 优惠券商品名称分页查询，有子集sku信息
	 *
	 * @param productQuery
	 * @return
	 */
	@GetMapping("/productPage")
	@PreAuthorize("@pms.hasPermission('s_product_coupon_productPage')")
	@Operation(summary = "【商家】优惠券商品名称分页查询", description = "")
	public R<PageSupport<ProductBO>> productPage(ProductQuery productQuery) {
		productQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		productQuery.setStatus(ProductStatusEnum.PROD_ONLINE.value());
		productQuery.setOpStatus(OpStatusEnum.PASS.getValue());
		return R.ok(couponService.productPage(productQuery));
	}

}
