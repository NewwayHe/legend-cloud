/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.service.PreSellProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预售商品表(PreSellProduct)表控制层
 *
 * @author legendshop
 * @since 2020-08-18 09:14:52
 */
@Tag(name = "预售商品管理")
@RestController
@RequestMapping(value = "/admin/preSellProduct", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminPreSellProductController {
	/**
	 * 服务对象
	 */
	@Autowired
	private PreSellProductService presellProductService;

	/**
	 * 根据id获取预售商品
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_presellProduct_get')")
	@Parameter(name = "id", description = "预售商品ID", required = true)
	@Operation(summary = "【后台】根据id获取预售商品")
	public R<PreSellProductDTO> getById(@PathVariable("id") Long id) {
		return R.ok(presellProductService.getById(id));
	}

	/**
	 * 根据id删除预售商品
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_presellProduct_del')")
	@Parameter(name = "id", description = "预售商品ID", required = true)
	@Operation(summary = "【后台】根据id删除预售商品")
	public R delById(@PathVariable("id") Long id) {
		return R.ok(presellProductService.deleteById(id));
	}

	/**
	 * 保存预售商品
	 *
	 * @param presellProductDTO
	 * @return
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_product_presellProduct_add')")
	@Operation(summary = "【后台】保存预售商品")
	public R save(@Valid @RequestBody PreSellProductDTO presellProductDTO) {
		return R.ok(presellProductService.save(presellProductDTO));
	}

	/**
	 * 更新预售商品
	 *
	 * @param presellProductDTO
	 * @return
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_product_presellProduct_update')")
	@Operation(summary = "【后台】更新预售商品")
	public R update(@Valid @RequestBody PreSellProductDTO presellProductDTO) {
		return R.ok(presellProductService.update(presellProductDTO));
	}
}
