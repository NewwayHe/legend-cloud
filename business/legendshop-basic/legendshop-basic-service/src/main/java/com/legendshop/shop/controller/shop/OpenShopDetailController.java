/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.legendshop.basic.api.AuditApi;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.OpenShopStatusEnum;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.shop.service.ShopDetailService;
import com.legendshop.shop.vo.ShopDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 商家入驻控制器.
 *
 * @author legendshop
 */
@Tag(name = "商家入驻")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/s/shop/detail", produces = MediaType.APPLICATION_JSON_VALUE)
public class OpenShopDetailController {

	private final AuditApi auditApi;

	private final ShopDetailService shopDetailService;


	/**
	 * 判断商家入驻状态 TODO 被平台下线或关闭的店铺应该执行怎样的操作，有待商议
	 * {@link com.legendshop.shop.enums.OpenShopStatusEnum;}  商家入驻状态码
	 */
	@Operation(summary = "【商家】判断商家入驻状态")
	@GetMapping("/open/shop/status")
	public R<Integer> openShopStatus() {
		Long shopUserId = SecurityUtils.getShopUser().getUserId();
		ShopDetailVO shopDetail = shopDetailService.getByShopUserId(shopUserId);
		if (ObjectUtil.isEmpty(shopDetail)) {
			//未提交入驻申请
			return R.ok(OpenShopStatusEnum.NOT_SUBMITTED.getStatus(), "未提交入驻申请");
		}
		if (ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getOpStatus())) {
			//审核通过,跳转首页
			return R.ok(OpenShopStatusEnum.APPROVED.getStatus(), "审核通过");
		}
		if (OpStatusEnum.WAIT.getValue().equals(shopDetail.getOpStatus())) {
			//待审核
			return R.ok(OpenShopStatusEnum.REVIEW.getStatus(), "待审核");
		}
		if (OpStatusEnum.DENY.getValue().equals(shopDetail.getOpStatus())) {
			//审核拒绝,返回拒绝原因
			String auditOpinion = auditApi.getAuditOptionByShopId(shopDetail.getId()).getData();
			return R.ok(OpenShopStatusEnum.REFUSE.getStatus(), auditOpinion);
		}
		return R.fail("店铺状态异常，请联系管理员");
	}

	@Operation(summary = "【商家】保存店铺信息")
	@PostMapping("/save/shop/info")
	public R<Void> saveShopInfo(@Valid @RequestBody ShopDetailDTO shopDetail, HttpServletRequest request) {
		//判断是新增还是编辑
		if (ObjectUtil.isNotEmpty(shopDetail.getId())) {
			//更新为待审核状态
			return updateSettleInInfo(shopDetail);
		}
		return insertShopInfo(shopDetail, request);
	}

	/**
	 * 新增店铺信息
	 */
	@PostMapping
	public R<Void> insertShopInfo(@Valid @RequestBody ShopDetailDTO shopDetail, HttpServletRequest request) {
		Long shopUserId = SecurityUtils.getShopUser().getUserId();
		if (!isShopUserIdExist(shopUserId).getSuccess()) {
			return R.fail("该用户已有店铺，不能重复申请");
		}
		shopDetail.setShopUserId(shopUserId);
		shopDetailService.saveShopDetail(shopDetail, JakartaServletUtil.getClientIP(request));
		return R.ok();
	}

	/**
	 * 编辑店铺信息
	 */
	@PutMapping
	public R<Void> updateSettleInInfo(@Valid @RequestBody ShopDetailDTO shopDetail) {
		return shopDetailService.updateSettleInInfo(shopDetail);
	}

	@Operation(summary = "【商家】判断店铺名称是否存在")
	@Parameter(name = "shopName", description = "店铺名", required = true)
	@GetMapping("/isShopNameExist")
	public R<Void> isShopNameExist(@RequestParam String shopName) {
		boolean result = shopDetailService.isShopNameExist(shopName.trim());
		if (result) {
			return R.fail("该店铺名已有，请重新输入");
		}
		return R.ok();
	}

	/**
	 * 判断用户Id是否存在
	 *
	 * @param shopUserId the 商家用户ID
	 * @return ok:已经存在  fail:不存在
	 */
	@GetMapping("/isShopUserIdExist")
	public R<Void> isShopUserIdExist(@RequestParam Long shopUserId) {
		boolean result = shopDetailService.isShopUserIdExist(shopUserId);
		if (result) {
			return R.fail("该用户已有店铺，不能重复申请");
		}
		return R.ok();
	}

	@Operation(summary = "【商家】重新编辑入驻界面")
	@GetMapping("/edit/shop")
	public R<ShopDetailVO> editShop() {
		Long shopUserId = SecurityUtils.getShopUser().getUserId();
		return R.ok(shopDetailService.getByShopUserId(shopUserId));
	}

}
