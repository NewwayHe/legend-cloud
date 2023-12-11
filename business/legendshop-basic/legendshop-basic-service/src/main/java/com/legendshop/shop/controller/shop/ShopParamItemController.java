/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.service.ShopParamItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家配置项(ShopParamItem)表控制层
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
@Tag(name = "店铺-配置项设置")
@RestController
@RequestMapping(value = "/shopParamItem", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopParamItemController {

	/**
	 * 商家配置项(ShopParamItem)服务对象
	 */
	@Autowired
	private ShopParamItemService shopParamItemService;

	/**
	 * 根据id查询商家配置项
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_get')")
	@Operation(summary = "【商家】根据id查询商家配置项")
	public R<ShopParamItemDTO> getById(@PathVariable("id") Long id) {
		return R.ok(shopParamItemService.getById(id));
	}

	/**
	 * 根据id删除商家配置项
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除商家配置项")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_del')")
	@Operation(summary = "【商家】根据id删除商家配置项")
	public R deleteById(@PathVariable("id") Long id) {
		return R.ok(shopParamItemService.deleteById(id));
	}

	/**
	 * 保存商家配置项
	 *
	 * @param shopParamItemDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存商家配置项")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_add')")
	@Operation(summary = "【商家】保存商家配置项")
	public R save(@Valid @RequestBody ShopParamItemDTO shopParamItemDTO) {
		return shopParamItemService.save(shopParamItemDTO);
	}

	/**
	 * 更新商家配置项
	 *
	 * @param shopParamItemDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商家配置项")
	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_update')")
	@Operation(summary = "【商家】更新商家配置项")
	public R update(@Valid @RequestBody ShopParamItemDTO shopParamItemDTO) {
		return R.ok(shopParamItemService.update(shopParamItemDTO));
	}

	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_item_updateValueOnlyById')")
	@Operation(summary = "【商家】批量修改配置项value")
	@PutMapping("/value/items")
	public R updateValueOnlyById(@Valid @RequestBody List<SysParamValueItemDTO> sysParamValueItemDTOS) {
		shopParamItemService.updateValueOnlyById(sysParamValueItemDTOS);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('s_product_ShopParamItem_item_queryItemList')")
	@Operation(summary = "【商家】根据主配置名称查询配置项")
	@GetMapping("/list")
	public R<List<ShopParamItemDTO>> getItemList(@RequestParam String shopParamName) {
		return R.ok(shopParamItemService.getByParentParamName(shopParamName, SecurityUtils.getShopUser().getShopId()));
	}
}
