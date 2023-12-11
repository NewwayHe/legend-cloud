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
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.dto.CategoryDTO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.query.ProductCategoryRelationQuery;
import com.legendshop.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类目管理
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/admin/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "类目管理")
public class AdminCategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 根据id查询类目
	 *
	 * @param id
	 * @return
	 */
	@Operation(summary = "【后台】根据id查询类目")
	@PreAuthorize("@pms.hasPermission('admin_product_category_get')")
	@Parameter(name = "id", description = "类目ID", required = true)
	@GetMapping("/{id}")
	public R<CategoryBO> getById(@PathVariable("id") Long id) {
		return R.ok(categoryService.getById(id));
	}

	/**
	 * 根据id删除类目
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@Parameter(name = "id", description = "类目ID", required = true)
	@SystemLog("根据id删除类目")
	@PreAuthorize("@pms.hasPermission('admin_product_category_del')")
	@Operation(summary = "【后台】根据id删除类目")
	public R delById(@PathVariable("id") Long id) {
		return categoryService.deleteById(id);
	}

	/**
	 * 保存平台类目
	 *
	 * @param categoryDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存平台类目")
	@PreAuthorize("@pms.hasPermission('admin_product_category_add')")
	@Operation(summary = "【后台】保存平台类目")
	public R save(@RequestBody @Valid CategoryDTO categoryDTO) {
		return R.ok(categoryService.save(categoryDTO));
	}

	/**
	 * 更新平台类目
	 *
	 * @param categoryDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新平台类目")
	@PreAuthorize("@pms.hasPermission('admin_product_category_update')")
	@Operation(summary = "【后台】更新平台类目")
	public R<String> update(@RequestBody @Valid CategoryDTO categoryDTO) {
		int row = categoryService.update(categoryDTO);
		if (row > 0) {
			return R.ok();
		}
		return R.fail();
	}


	/**
	 * 修改平台类目状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@SystemLog("修改平台类目状态")
	@Operation(summary = "【后台】修改平台类目状态")
	@Parameters({
			@Parameter(name = "id", description = "类目ID", required = true),
			@Parameter(name = "status", description = "状态", required = true)
	})
	@PutMapping("/changeStatus/{id}/{status}")
	@PreAuthorize("@pms.hasPermission('admin_product_category_updateStatus')")
	public R<String> updateStatus(@PathVariable Long id, @PathVariable Integer status, Boolean includeProduct) {
		return categoryService.updateStatus(status, id, includeProduct);
	}

	/**
	 * 根据父级id查询类目
	 *
	 * @param parentId
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【后台】根据父级id查询类目")
	@PreAuthorize("@pms.hasPermission('admin_product_category_queryByParentId')")
	@Parameter(name = "parentId", description = "父级id", required = true)
	public R<List<CategoryTree>> queryByParentId(Long parentId) {
		// 获取符合条件的分类
		Set<CategoryBO> all = new HashSet<>();
		/*	status 上线状态【0：下线，1：上线，2：全部】 */
		all.addAll(categoryService.queryByParentId(parentId, 2));
		return R.ok(categoryService.filterCategory(all, parentId));
	}

	/**
	 * 简单查询所有在线的类目树
	 *
	 * @return
	 */
	@GetMapping("/briefTree")
	@Operation(summary = "【后台】简单查询所有在线的类目树")
	@PreAuthorize("@pms.hasPermission('admin_product_category_queryAllOnLine')")
	public R<List<CategoryTree>> queryAllOnLine() {
		// 获取符合条件的分类
		Set<CategoryBO> all = new HashSet<>();
		all.addAll(categoryService.queryAllOnline());
		return R.ok(categoryService.filterCategory(all, -1L));
	}

	/**
	 * 【后台】查看类目下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	@Operation(summary = "【后台】查看类目下的商品列表")
	@GetMapping("/product")
	public R<PageSupport<ProductCategoryRelationBO>> queryProductList(@Valid ProductCategoryRelationQuery relationQuery) {
		return R.ok(categoryService.queryProductList(relationQuery));
	}

	/**
	 * 简单查询所有在线的类目树
	 *
	 * @return
	 */
	@GetMapping("/productPropertyAgg/briefTree")
	@Operation(summary = "【后台】查询所有在线的未被其它规格参数组使用的类目树")
	@PreAuthorize("@pms.hasPermission('admin_product_category_queryAllOnLine')")
	public R<List<CategoryTree>> queryAllOnLine(Long productPropertyAggId) {
		// 获取符合条件的分类
		Set<CategoryBO> all = new HashSet<>();
		all.addAll(categoryService.queryAllOnlineByProductPropertyAggId(productPropertyAggId));
		return R.ok(categoryService.filterCategory(all, -1L));
	}

}
