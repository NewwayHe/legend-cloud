/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ProductPropertyQuery;
import com.legendshop.product.service.ProductPropertyService;
import com.legendshop.product.service.ProductPropertyValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
@RequestMapping(value = "/s/productProperty", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopProductPropertyController {

	@Autowired
	private ProductPropertyService productPropertyService;

	@Autowired
	private ProductPropertyValueService propertyValueService;

	/**
	 * 根据id查询商品属性
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_get')")
	@Parameter(name = "id", description = "商品属性ID", required = true)
	@Operation(summary = "【商家】根据id查询商品属性")
	public R<ProductPropertyBO> getById(@PathVariable("id") Long id) {
		return R.ok(productPropertyService.getDetailById(id));
	}

	/**
	 * 根据id集合查询商品属性
	 *
	 * @param ids
	 * @return
	 */
	@GetMapping("/getByIds")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_getByIds')")
	@Parameter(name = "ids", description = "商品属性ID", required = true)
	@Operation(summary = "【商家】根据id集合查询商品属性")
	public R<List<ProductPropertyBO>> getById(@RequestParam Long[] ids) {
		return R.ok(productPropertyService.getDetailByIds(CollUtil.toList(ids)));
	}

	/**
	 * 根据id删除商品属性
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除商品属性")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_del')")
	@Parameter(name = "id", description = "商品属性ID", required = true)
	@Operation(summary = "【商家】根据id删除商品属性")
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
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_queryByGroupID')")
	@Parameter(name = "id", description = "参数组ID", required = true)
	@Operation(summary = "【商家】根据参数组id查询参数属性")
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
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_add')")
	@Operation(summary = "【商家】保存商品属性")
	public R<String> save(@Valid @RequestBody ProductPropertyDTO productPropertyDTO) {
		productPropertyDTO.setShopId(SecurityUtils.getShopUser().getShopId());
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
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_update')")
	@Operation(summary = "【商家】更新商品属性")
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
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_page')")
	@Operation(summary = "【商家】商品属性分页查询")
	public R<PageSupport<ProductPropertyBO>> page(@Valid ProductPropertyQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		query.setSource(ProductPropertySourceEnum.USER.value());
		return R.ok(productPropertyService.queryPage(query));
	}

	/**
	 * 根据类目id、商品id查询相关联的参数属性
	 *
	 * @return
	 */
	@GetMapping("/queryParamByCategoryId/{id}")
	@Operation(summary = "【商家】根据分类id、商品id查询参数列表")
	@Parameters({
			@Parameter(name = "id", description = "商品类目ID", required = true),
			@Parameter(name = "productId", description = "商品ID", required = false)
	})
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_queryParamByCategoryId')")
	public R<List<ProductPropertyBO>> queryParamByCategoryId(@PathVariable Long id, Long productId) {
		return R.ok(productPropertyService.queryParamByCategoryId(id, productId));
	}

	/**
	 * 根据分类id、商品id查询规格列表
	 *
	 * @return
	 */
	@GetMapping("/querySpecificationByCategoryId/{id}")
	@Parameters({
			@Parameter(name = "id", description = "商品类目ID", required = true),
			@Parameter(name = "productId", description = "商品ID", required = false)
	})
	@Operation(summary = "【商家】根据分类id、商品id查询规格列表")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_querySpecificationByCategoryId')")
	public R<List<ProductPropertyDTO>> querySpecificationByCategoryId(@PathVariable Long id, Long productId) {
		return R.ok(productPropertyService.querySpecificationByCategoryId(id, productId));
	}

	/**
	 * 获取自定义属性值（规格属性、参数属性）id
	 *
	 * @return
	 */
	@GetMapping("/userPropertyValueId")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_getUserPropertyValueId')")
	@Operation(summary = "【商家】获取自定义属性值（规格属性、参数属性）id")
	public R getUserPropertyValueId() {
		return R.ok(propertyValueService.createId());
	}

	/**
	 * 获取自定义属性名称（规格属性、参数属性）id
	 *
	 * @return
	 */
	@GetMapping("/userPropertyNameId")
	@PreAuthorize("@pms.hasPermission('s_product_productProperty_getUserPropertyNameId')")
	@Operation(summary = "【商家】获取自定义属性名称（规格属性、参数属性）id")
	public R getUserPropertyNameId() {
		return R.ok(productPropertyService.createId());
	}

}
