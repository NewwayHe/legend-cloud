/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.dto.ShopCategoryDTO;
import com.legendshop.product.dto.ShopCategoryTree;
import com.legendshop.product.query.ProductCategoryRelationQuery;
import com.legendshop.product.service.CategoryService;
import com.legendshop.product.service.ShopCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author legendshop
 */
@Tag(name = "类目管理")
@RestController
@Slf4j
@RequestMapping(value = "/s/shopCategory", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopShopCategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	/**
	 * 根据id查询类目
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_get')")
	@Parameter(name = "id", description = "类目ID", required = true)
	@Operation(summary = "【商家】根据id查询类目")
	public R<ShopCategoryDTO> getById(@PathVariable("id") Long id) {
		return R.ok(shopCategoryService.getById(id));
	}

	/**
	 * 根据id删除店铺商品分类
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_del')")
	@Parameter(name = "id", description = "类目ID", required = true)
	@Operation(summary = "【商家】根据id删除店铺商品分类")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "根据id删除店铺商品分类")
	public R delById(@PathVariable("id") Long id) {
		return shopCategoryService.deleteById(id);
	}

	/**
	 * 保存店铺商品分类
	 *
	 * @param shopCategoryDTO
	 * @return
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_add')")
	@Operation(summary = "【商家】保存店铺商品分类")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "保存店铺商品分类")
	public R save(@RequestBody @Valid ShopCategoryDTO shopCategoryDTO) {
		shopCategoryDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		return shopCategoryService.save(shopCategoryDTO);
	}

	/**
	 * 修改店铺商品分类
	 *
	 * @param shopCategoryDTO
	 * @return
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_update')")
	@Operation(summary = "【商家】修改店铺商品分类")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "修改店铺商品分类")
	public R update(@RequestBody @Valid ShopCategoryDTO shopCategoryDTO) {
		return shopCategoryService.update(shopCategoryDTO);
	}

	/**
	 * 修改店铺商品分类状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Operation(summary = "【商家】修改店铺商品分类状态")
	@PutMapping("/changeStatus/{id}/{status}")
	@Parameters({
			@Parameter(name = "id", description = "类目ID", required = true),
			@Parameter(name = "status", description = "状态", required = true)
	})
	@SystemLog(type = SystemLog.LogType.SHOP, value = "修改店铺商品分类状态")
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_changeStatus')")
	public R changeStatus(@PathVariable Long id, @PathVariable Integer status) {
		return shopCategoryService.updateStatus(status, id);
	}

	/**
	 * 获取平台的在线同级类目
	 *
	 * @return
	 */
	@Operation(summary = "【商家】简单查询所有在线的平台类目树")
	@GetMapping("/getPlatformCategory")
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_getPlatformCategory')")
	public R<List<CategoryTree>> getPlatformCategory() {
		// 获取符合条件的类目
		Set<CategoryBO> all = new HashSet<>();
		all.addAll(categoryService.queryAllOnline());
		return R.ok(categoryService.filterCategory(all, null));
	}

	/**
	 * 获取店铺的所有类目,状态 1上线，2下线，3全部
	 *
	 * @param status
	 * @return
	 */
	@GetMapping
	@PreAuthorize("@pms.hasPermission('s_product_shopCategory_getCategoryByParentId')")
	@Operation(summary = "【商家】获取店铺的所有类目")
	@Parameter(name = "status", description = "状态 1上线，2下线，3全部", required = true)
	public R<List<ShopCategoryTree>> getCategoryByParentId(@NotNull(message = "状态不能为空") int status) {
		// 获取符合条件的类目
		Set<ShopCategoryBO> all = new HashSet<>();
		Long shopId = SecurityUtils.getShopUser().getShopId();
		all.addAll(shopCategoryService.queryByShopId(shopId, status));
		return R.ok(shopCategoryService.filterShopCategory(all));
	}


	/**
	 * 根据父级id查询类目
	 *
	 * @param parentId
	 * @return
	 */
	@GetMapping("/queryListByParentId")
	@Operation(summary = "【商家】根据父级id查询类目")
	@Parameter(name = "parentId", description = "父级id", required = true)
	public R<List<CategoryBO>> queryListByParentId(Long parentId, String name) {
		/*	status 上线状态【0：下线，1：上线，2：全部】 */
		List<CategoryBO> categoryBOS = categoryService.queryByParentIdAndName(parentId, name, 1);
		return R.ok(categoryBOS);
	}

	/**
	 * 【商家】查看类目下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	@Operation(summary = "【商家】查看类目下的商品列表")
	@GetMapping("/product")
	public R<PageSupport<ProductCategoryRelationBO>> queryProductList(ProductCategoryRelationQuery relationQuery) {
		relationQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(categoryService.queryProductList(relationQuery));
	}
}
