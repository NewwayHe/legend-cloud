/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.common;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.dto.ShopParamsDTO;
import com.legendshop.shop.service.ShopParamsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/shopParams")
public class ShopParamsController {

	final ShopParamsService shopParamsService;


	@PostMapping(value = "/getConfigDtoByParamName")
	public <T> R<T> getConfigDtoByParamName(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "name") String name, @RequestBody Class<T> clazz) {
		T configDtoByParamName = this.shopParamsService.getConfigDtoByParamName(shopId, name, clazz);
		return R.ok(configDtoByParamName);
	}

	@GetMapping(value = "/getSysParamItemsByParamName")
	public R<List<ShopParamItemDTO>> getSysParamItemsByParamName(@RequestParam(value = "name") String name, @RequestParam(value = "shopId") Long shopId) {
		return R.ok(this.shopParamsService.getShopParamItemsByParamName(name, shopId));
	}

	/**
	 * 根据id查询商家主配置
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParams_get')")
	@Operation(summary = "【商家】根据id查询商家主配置")
	public R<ShopParamsDTO> getById(@PathVariable("id") Long id) {
		return R.ok(shopParamsService.getById(id));
	}

	/**
	 * 根据id删除商家主配置
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除商家主配置")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParams_del')")
	@Operation(summary = "【商家】根据id删除商家主配置")
	public R deleteById(@PathVariable("id") Long id) {
		return R.ok(shopParamsService.deleteById(id));
	}

	/**
	 * 保存商家主配置
	 *
	 * @param shopParamsDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存商家主配置")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParams_add')")
	@Operation(summary = "【商家】保存商家主配置")
	public R save(@Valid @RequestBody ShopParamsDTO shopParamsDTO) {
		return shopParamsService.save(shopParamsDTO);
	}

	/**
	 * 更新商家主配置
	 *
	 * @param shopParamsDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商家主配置")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParams_update')")
	@Operation(summary = "【商家】更新商家主配置")
	public R update(@Valid @RequestBody ShopParamsDTO shopParamsDTO) {
		return R.ok(shopParamsService.update(shopParamsDTO));
	}
}
