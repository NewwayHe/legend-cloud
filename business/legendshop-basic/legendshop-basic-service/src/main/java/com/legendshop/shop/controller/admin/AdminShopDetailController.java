/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.shop.enums.ShopTypeEnum;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.service.ShopDetailService;
import com.legendshop.shop.vo.ShopDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 店铺列表控制器
 *
 * @author legendshop
 */
@Tag(name = "店铺列表控制器")
@RestController
@RequestMapping(value = "/admin/shop/detail", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminShopDetailController {

	private final ShopDetailService shopDetailService;

	@GetMapping("/page")
	@Operation(summary = "【后台】分页查询店铺信息")
	public R<PageSupport<ShopDetailDTO>> page(ShopDetailQuery shopDetailQuery) {
		return R.ok(this.shopDetailService.page(shopDetailQuery));
	}

	@PutMapping("/updateStatus")
	@Operation(summary = "【后台】批量修改状态")
	public R updateStatus(@RequestBody BasicBatchUpdateStatusDTO dto) {
		if (CollUtil.isEmpty(dto.getIds())) {
			return R.fail("ids不能为空");
		}
		if (ShopDetailStatusEnum.getStatusEnum(dto.getStatus()) == null) {
			return R.fail("状态不匹配");
		}
		return shopDetailService.batchUpdateStatus(dto);
	}

	@PutMapping("/audit")
	@SystemLog("审核店铺")
	@Operation(summary = "【后台】审核店铺")
	public R audit(@Valid @RequestBody AuditDTO auditDTO) {
		if (ObjectUtil.isEmpty(auditDTO.getCommonId()) && CollUtil.isEmpty(auditDTO.getIdList())) {
			return R.fail("店铺id不能为空");
		}
		if (CollUtil.isEmpty(auditDTO.getIdList())) {
			auditDTO.setIdList(Collections.singletonList(auditDTO.getCommonId()));
		}
		return this.shopDetailService.audit(auditDTO);
	}


	@Operation(summary = "【后台】获取店铺详情")
	@Parameter(name = "id", description = "店铺id", required = true)
	@GetMapping("/{id}")
	public R<ShopDetailVO> getById(@PathVariable Long id) {
		return R.ok(this.shopDetailService.getShopDetailVO(id));
	}


	@Operation(summary = "【后台】修改店铺信息")
	@SystemLog("修改店铺信息")
	@PutMapping("/updateDetail")
	public R updateDetail(@Valid @RequestBody ShopDetailDTO detailDTO) {
		return shopDetailService.updateShopDetail(detailDTO);
	}

	@Operation(summary = "【后台】修改店铺类型")
	@Parameters({
			@Parameter(name = "id", description = "店铺id"),
			@Parameter(name = "type", description = "类型 0.专营店1.旗舰店2.自营店")
	})
	@SystemLog("修改店铺类型")
	@PostMapping("/updateShopType")
	public R updateShopType(@RequestParam Long id, @RequestParam Integer type) {
		if (!ShopTypeEnum.existValue(type)) {
			return R.fail("店铺类型不匹配");
		}
		return R.ok(shopDetailService.updateShopType(id, type));
	}


	@Operation(summary = "【后台】修改店铺佣金比例")
	@Parameters({
			@Parameter(name = "id", description = "店铺id"),
			@Parameter(name = "commissionRate", description = "佣金比例")
	})
	@SystemLog("修改店铺佣金比例")
	@PostMapping("/updateCommissionRate")
	public R updateCommissionRate(@RequestParam Long id, @RequestParam Double commissionRate) {
		if (commissionRate == null || commissionRate < 0 || commissionRate > 100) {
			return R.fail("佣金比例要在0~100之间");
		}
		return R.ok(shopDetailService.updateCommissionRate(id, commissionRate));
	}

	@Operation(summary = "【后台】修改店铺商品审核")
	@Parameters({
			@Parameter(name = "id", description = "店铺id"),
			@Parameter(name = "status", description = "商品是否需要审核 1:是 0:否 为空则采用平台总设置")
	})
	@SystemLog("修改店铺商品审核")
	@PostMapping("/updateProdAudit")
	public R updateProdAudit(@RequestParam Long id, @RequestParam Integer status) {
		if (status != 1 && status != 0) {
			return R.fail("审核类型不匹配");
		}
		return R.ok(shopDetailService.updateProdAudit(id, status));
	}


	@Operation(summary = "【后台】获取审核店铺的历史记录")
	@GetMapping("/{shopId}/audit/history")
	public R<PageSupport<AuditDTO>> auditHistory(@PathVariable Long shopId, PageParams pageParams) {
		return R.ok(this.shopDetailService.auditHistory(shopId, pageParams));
	}


	@Operation(summary = "【后台】查询所有在线店铺信息")
	@GetMapping("/allOnline")
	public R<List<ShopDetailDTO>> onlineList() {
		return R.ok(this.shopDetailService.queryOnlineList());
	}


	@Parameters({
			@Parameter(name = "shopId", description = "店铺id"),
			@Parameter(name = "shopNewBieStatus", description = "新手引导状态,0:新手,1非新手")
	})
	@Operation(summary = "新手引导")
	@GetMapping("/newBier")
	public int shopNewBie(@RequestParam Integer shopNewBieStatus, Long shopId) {
		return shopDetailService.updateShopNewBieStatus(shopNewBieStatus, shopId);
	}

}
