/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ProductPropertyQuery;
import com.legendshop.product.service.ProductPropertyService;
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
 * 商品属性管理（规格属性、参数属性【可搜索】）
 *
 * @author legendshop
 */
@Tag(name = "商品属性管理（规格属性、参数属性)")
@RestController
@RequestMapping(value = "/admin/productProperty", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductPropertyController {

	@Autowired
	private ProductPropertyService productPropertyService;

	/**
	 * 根据id查询商品属性
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Operation(summary = "【后台】根据id查询商品属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_get')")
	@Parameter(name = "id", description = "商品属性ID", required = true)
	public R<ProductPropertyBO> getById(@PathVariable("id") Long id) {
		return R.ok(productPropertyService.getDetailById(id));
	}

	/**
	 * 根据id删除商品属性
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除商品属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_del')")
	@Operation(summary = "【后台】根据id删除商品属性")
	@Parameter(name = "id", description = "商品属性ID", required = true)
	public R<String> delById(@PathVariable("id") Long id) {
		productPropertyService.deleteById(id);
		return R.ok();
	}


	/**
	 * 根据参数组id查询参数属性
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/queryByGroup/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_listByGroupId')")
	@Operation(summary = "【后台】根据参数组id查询参数属性")
	@Parameter(name = "id", description = "参数组ID", required = true)
	public R<List<ProductPropertyBO>> queryByGroupId(@PathVariable("id") Long id) {
		return R.ok(productPropertyService.queryByGroupId(id));
	}

	/**
	 * 保存商品属性
	 *
	 * @param productPropertyDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存商品属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_add')")
	@Operation(summary = "【后台】保存商品属性")
	public R<String> save(@Valid @RequestBody ProductPropertyDTO productPropertyDTO) {
		if (productPropertyService.save(productPropertyDTO)) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 更新商品属性
	 *
	 * @param productPropertyDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商品属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_update')")
	@Operation(summary = "【后台】更新商品属性")
	public R<String> update(@Valid @RequestBody ProductPropertyDTO productPropertyDTO) {
		if (productPropertyService.update(productPropertyDTO)) {
			return R.ok();
		}
		return R.fail("更新失败");
	}

	/**
	 * 商品属性分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【后台】商品属性分页查询")
	@PreAuthorize("@pms.hasPermission('admin_product_productProperty_page')")
	public R<PageSupport<ProductPropertyBO>> page(@Valid ProductPropertyQuery query) {
		query.setSource(ProductPropertySourceEnum.SYSTEM.value());
		return R.ok(productPropertyService.queryPage(query));
	}

}
