/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.user;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponShopBO;
import com.legendshop.activity.bo.MyCouponBO;
import com.legendshop.activity.bo.MyCouponCountBO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.dto.OrderRefundCouponDTO;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponShopQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.data.dto.CouponViewDTO;
import com.legendshop.product.bo.SkuBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/p/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户优惠券管理")
public class UserCouponController {

	private final CouponUserService couponUserService;
	private final CouponService couponService;
	private final UserTokenUtil userTokenUtil;

	/**
	 * 根据id查询优惠券详情
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/detail")
	@Parameters({
			@Parameter(name = "id", description = "优惠券ID", required = true),
			@Parameter(name = "couponUserId", description = "用户优惠券ID", required = true)
	})
	@Operation(summary = "【用户】根据id查询优惠券详情", description = "")
	public R<CouponDTO> getDetailById(Long id, Long couponUserId, HttpServletRequest request) {
		Long userId = userTokenUtil.getUserId(request);
		if (userId != null && userId == 0L) {
			userId = null;
		}

		R<CouponDTO> byId = couponService.getById(id, couponUserId, userId);
		if (ObjectUtil.isNotEmpty(byId)) {
			CouponViewDTO couponViewDTO = new CouponViewDTO();
			couponViewDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
			couponViewDTO.setCouponId(id);
			couponViewDTO.setUserId(userId);
			DateTime Time = DateUtil.offsetDay(new Date(), 0);
			couponViewDTO.setCreateTime(Time);
			couponUserService.updateVisit(couponViewDTO);
		}
		return couponService.getById(id, couponUserId, userId);
	}

	/**
	 * 根据id查询用户优惠券
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "优惠券ID", required = true)
	@Operation(summary = "【用户】根据id查询优惠券", description = "")
	public R<CouponDTO> getById(@PathVariable("id") Long id) {
		return couponUserService.getById(id, SecurityUtils.getUserId());
	}

	/**
	 * 用户优惠券列表
	 *
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【用户】用户优惠券列表", description = "")
	public R<PageSupport<MyCouponBO>> page(CouponQuery couponQuery) {
		couponQuery.setUserId(SecurityUtils.getUserId());
		//分页查询用户所有已领取的优惠券列表
		return R.ok(couponUserService.queryMyCouponPage(couponQuery));
	}


	/**
	 * 用户领券
	 *
	 * @param couponId
	 * @return
	 */
	@PostMapping("/{couponId}")
	@Parameter(name = "couponId", description = "优惠券ID", required = true)
	@Operation(summary = "【用户】用户领券", description = "")
	public R receiveCoupon(@PathVariable Long couponId) {
		Long userId = SecurityUtils.getUserId();
		if (ObjectUtil.isEmpty(userId)) {
			return R.fail("请先登录");
		}
		//用户领取
		return couponUserService.save(couponId, userId);
	}

	/**
	 * 卡密领券
	 *
	 * @param pwd
	 * @return
	 */
	@GetMapping("active/{pwd}")
	@Parameter(name = "pwd", description = "卡密", required = true)
	@Operation(summary = "【用户】卡密领券", description = "")
	public R receiveCoupon(@PathVariable String pwd) {
		Long userId = SecurityUtils.getUserId();
		//用户卡密领券
		return couponUserService.updateUserIdByPwd(pwd, userId);
	}

	/**
	 * 优惠券关联的商品列表
	 *
	 * @param couponQuery 优惠券id
	 * @return
	 */
	@Operation(summary = "【用户】优惠券关联的商品列表", description = "")
	@GetMapping("/querySkuPageById")
	public R<PageSupport<SkuBO>> productList(CouponQuery couponQuery) {

		//userTokenUtil
		Long userId = SecurityUtils.getUserId();
		couponQuery.setUserId(userId);
		return R.ok(couponService.queryCouponProductPage(couponQuery));

	}


	/**
	 * 优惠券关联的店铺列表
	 *
	 * @param couponQuery 优惠券id
	 * @return
	 */
	@Operation(summary = "【用户】优惠券关联的店铺列表", description = "")
	@GetMapping("/queryShopPage")
	public R<PageSupport<CouponShopBO>> queryShopPage(CouponShopQuery couponQuery) {
		return couponService.queryCouponShopPage(couponQuery);
	}


	@GetMapping("/userCouponCount")
	public R<Integer> userCouponCount(@RequestParam(value = "userId") Long userId) {
		return couponUserService.userCouponCount(userId);
	}


	@GetMapping("/getByOrderNumber")
	public R<List<CouponUserDTO>> getByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber) {
		return R.ok(couponUserService.getByOrderNumber(orderNumber));
	}

	@PutMapping("/update")
	public R<Void> update(@RequestBody List<CouponUserDTO> couponUsers) {
		couponUserService.update(couponUsers);
		return R.ok();
	}

	@PostMapping("/updateOrder")
	public R<Void> updateOrder(@RequestBody List<OrderRefundCouponDTO> couponUsers) {
//		couponUserService.updateCoupon(couponUsers);
		couponUserService.updateCouponOrder(couponUsers);
		couponService.updateUseCount(couponUsers);
		return R.ok();
	}


	@GetMapping("/getUserCouponCount")
	@Operation(summary = "【用户】用户优惠券数量", description = "")
	public R<MyCouponCountBO> getUserCouponCount() {
		Long userId = SecurityUtils.getUserId();
		//获取用户优惠券数量
		return couponUserService.getUserCouponCount(userId);
	}
}
