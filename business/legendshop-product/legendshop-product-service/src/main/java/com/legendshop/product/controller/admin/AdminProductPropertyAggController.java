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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.bo.ProductPropertyAggParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyAggBatchDTO;
import com.legendshop.product.dto.ProductPropertyAggDTO;
import com.legendshop.product.query.ProductPropertyAggQuery;
import com.legendshop.product.service.*;
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
 * @author legendshop
 */
@Tag(name = "类目关联管理")
@RestController
@RequestMapping(value = "/admin/productPropertyAgg", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductPropertyAggController {

	@Autowired
	private ProductPropertyAggService aggService;

	@Autowired
	private ProductPropertyAggBrandService aggBrandService;

	@Autowired
	private ProductPropertyAggParamService aggParamService;

	@Autowired
	private ProductPropertyAggParamGroupService aggParamGroupService;

	@Autowired
	private ProductPropertyAggSpecificationService aggSpecificationService;

	/**
	 * 保存类目关联
	 *
	 * @param productType
	 * @return
	 */
	@PostMapping
	@SystemLog("保存类目关联")
	@Operation(summary = "【后台】保存类目关联")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_add')")
	public R<String> save(@Valid @RequestBody ProductPropertyAggDTO productType) {
		Long result = aggService.save(productType);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 更新类目关联
	 *
	 * @param productType
	 * @return
	 */
	@PutMapping
	@SystemLog("更新类目关联")
	@Operation(summary = "【后台】更新类目关联")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_update')")
	public R<String> update(@Valid @RequestBody ProductPropertyAggDTO productType) {
		int result = aggService.update(productType);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 根据id查询类目关联管理
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "类目关联ID", required = true)
	@Operation(summary = "【后台】根据id查询类目关联管理")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_get')")
	public R<ProductPropertyAggBO> getById(@PathVariable Long id) {
		return R.ok(aggService.getById(id));
	}

	/**
	 * 根据id删除类目关联详情
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/detail/{id}")
	@Parameter(name = "id", description = "类目关联ID", required = true)
	@Operation(summary = "【后台】根据id删除类目关联详情")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_getDetail')")
	public R<ProductPropertyAggBO> getDetailById(@PathVariable Long id) {
		return R.ok(aggService.getDetailById(id));
	}

	/**
	 * 根据id查询规格属性
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/specification")
	@Operation(summary = "【后台】根据id查询规格属性")
	@PreAuthorize("@pms.hasPermission('admin_product_prodPropAgg_getSpec')")
	public R<PageSupport<ProductPropertyBO>> getSpecificationById(ProductPropertyAggQuery query) {
		return R.ok(aggService.getSpecificationById(query));
	}

	/**
	 * 根据id查询参数属性
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/param")
	@Operation(summary = "【后台】根据id查询参数属性")
	@PreAuthorize("@pms.hasPermission('admin_product_prodProp_getParam')")
	public R<PageSupport<ProductPropertyBO>> getParamById(ProductPropertyAggQuery query) {
		return R.ok(aggService.getParamById(query));
	}

	/**
	 * 查询所有类目关联管理
	 *
	 * @return
	 */
	@GetMapping("/listAll")
	@Operation(summary = "【后台】查询所有类目关联管理")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_listAll')")
	public R<List<ProductPropertyAggDTO>> listAll() {
		return R.ok(aggService.listAll());
	}

	/**
	 * 根据id查询参数组属性
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/paramGroup")
	@Operation(summary = "【后台】根据id查询参数组属性")
	@PreAuthorize("@pms.hasPermission('admin_product_prodProp_getParamGroup')")
	public R<PageSupport<ProductPropertyAggParamGroupBO>> getParamGroupById(ProductPropertyAggQuery query) {
		return R.ok(aggService.getParamGroupById(query));
	}

	/**
	 * 根据类目关联id查询品牌
	 *
	 * @param query
	 * @return
	 */

	@GetMapping("/brand")
	@Operation(summary = "【后台】根据类目关联id查询品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_prodProp_getBrand')")
	public R<PageSupport<BrandBO>> getBrandById(ProductPropertyAggQuery query) {
		return R.ok(aggService.getBrandById(query));
	}


	/**
	 * 根据id删除类目关联
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除类目关联")
	@Parameter(name = "id", description = "类目关联ID", required = true)
	@Operation(summary = "【后台】根据id删除类目关联")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_del')")
	public R<String> delById(@PathVariable Long id) {
		int result = aggService.deleteById(id);
		if (result > 0) {
			return R.ok("删除" + id + "成功");
		}
		return R.fail("删除" + id + "失败");

	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【后台】分页查询")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_page')")
	public R<PageSupport<ProductPropertyAggBO>> page(ProductPropertyAggQuery query) {
		return R.ok(aggService.queryPage(query));
	}

	/**
	 * 查询未关联的类目关联
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/simplePage")
	@Operation(summary = "【后台】查询未关联的类目关联")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_simplePage')")
	public R<PageSupport<ProductPropertyAggBO>> simplePage(ProductPropertyAggQuery query) {
		return R.ok(aggService.querySimplePage(query));
	}

	/**
	 * 删除关联品牌
	 *
	 * @param id
	 * @return
	 */
	@SystemLog("删除关联品牌")
	@DeleteMapping("/brand/{id}")
	@Parameter(name = "id", description = "主键", required = true)
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_delBrand')")
	@Operation(summary = "【后台】删除关联品牌")
	public R<String> delBrand(@PathVariable Long id) {
		int result = aggBrandService.deleteById(id);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("删除失败");
	}

	/**
	 * 保存关联规格属性
	 *
	 * @param aggDTO
	 * @return
	 */
	@SystemLog("保存关联规格属性")
	@PostMapping("/specification")
	@Operation(summary = "【后台】保存关联规格属性")
	@PreAuthorize("@pms.hasPermission('admin_product_prodPropAgg_saveSpec')")
	public R<String> saveSpecification(@RequestBody ProductPropertyAggBatchDTO aggDTO) {
		boolean result = aggSpecificationService.save(aggDTO);
		if (result) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 删除关联规格属性
	 *
	 * @param id
	 * @return
	 */
	@SystemLog("删除关联规格属性")
	@DeleteMapping("/specification/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_prodPropAgg_delSpec')")
	@Parameter(name = "id", description = "主键", required = true)
	@Operation(summary = "【后台】删除关联规格属性")
	public R<String> delSpecification(@PathVariable Long id) {
		int result = aggSpecificationService.deleteById(id);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("删除失败");
	}

	/**
	 * 保存关联参数属性
	 *
	 * @param aggDTO
	 * @return
	 */
	@PostMapping("/param")
	@SystemLog("保存关联参数属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_saveParam')")
	@Operation(summary = "【后台】保存关联参数属性")
	public R<String> saveParam(@RequestBody ProductPropertyAggBatchDTO aggDTO) {
		boolean result = aggParamService.save(aggDTO);
		if (result) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 删除关联参数属性
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/param/{id}")
	@Parameter(name = "id", description = "主键", required = true)
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_delParam')")
	@SystemLog("删除关联参数属性")
	@Operation(summary = "【后台】删除关联参数属性")
	public R<String> delParam(@PathVariable Long id) {
		int result = aggParamService.deleteById(id);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("删除失败");
	}

	/**
	 * 保存关联参数组属性
	 *
	 * @param aggDTO
	 * @return
	 */
	@PostMapping("/paramGroup")
	@SystemLog("保存关联参数组属性")
	@Operation(summary = "【后台】保存关联参数组属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_saveParamGroup')")
	public R<String> saveParamGroup(@RequestBody ProductPropertyAggBatchDTO aggDTO) {
		boolean result = aggParamGroupService.save(aggDTO);
		if (result) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 删除关联参数组属性
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/paramGroup/{id}")
	@SystemLog("删除关联参数组属性")
	@PreAuthorize("@pms.hasPermission('admin_product_productPropertyAgg_delParamGroup')")
	@Parameter(name = "id", description = "主键", required = true)
	@Operation(summary = "【后台】删除关联参数组属性")
	public R<String> delParamGroup(@PathVariable Long id) {
		int result = aggParamGroupService.deleteById(id);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("删除失败");
	}


}
