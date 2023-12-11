/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.validator.group.Save;
import com.legendshop.common.core.validator.group.Update;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.dto.ProductGroupDTO;
import com.legendshop.product.dto.ProductGroupRelationDTO;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;
import com.legendshop.product.service.ProductGroupRelationService;
import com.legendshop.product.service.ProductGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "商品分组")
@RestController
@RequestMapping(value = "/admin/product/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductGroupController {

	@Autowired
	private ProductGroupService productGroupService;

	@Autowired
	private ProductGroupRelationService relationService;


	@Operation(summary = "【后台】分页条件查询")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_page')")
	@GetMapping("/page")
	public R<PageSupport<ProductGroupDTO>> page(ProductGroupQuery productGroupQuery) {
		return R.ok(productGroupService.page(productGroupQuery));
	}

	@Operation(summary = "【后台】新增商品分组")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_add')")
	public R save(@Valid @RequestBody ProductGroupDTO productGroupDTO) {
		//类型默认自定义
		productGroupDTO.setType(1);
		productGroupDTO.setCreateTime(DateUtil.date());
		return R.ok(productGroupService.save(productGroupDTO));
	}

	@Operation(summary = "【后台】编辑商品分组")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_update')")
	public R update(@Valid @RequestBody ProductGroupDTO productGroupDTO) {
		productGroupDTO.setUpdateTime(DateUtil.date());
		if (productGroupService.update(productGroupDTO)) {
			return R.ok();
		}
		return R.fail();
	}

	@Parameter(name = "id", description = "主键id", required = true)
	@Operation(summary = "【后台】删除商品分组")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_del')")
	public R delete(@PathVariable Long id) {
		if (productGroupService.delGroupAndRelation(id)) {
			return R.ok();
		}
		return R.fail();
	}

	@Operation(summary = "【后台】查看分组下的商品列表")
	@GetMapping("/product")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_pageProductByGroupId')")
	public R<PageSupport<ProductGroupRelationBO>> queryProductList(ProductGroupRelationQuery relationQuery) {
		return R.ok(productGroupService.queryProductList(relationQuery));
	}

	@Operation(summary = "【后台】获取分组关联的商品id列表")
	@Parameter(name = "id", description = "分组id", required = true)
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_listProductIDByGroupId')")
	@GetMapping("/productIds")
	public R<List<Long>> admin_product_productGroup_listProductIDByGroupId(@RequestParam Long id) {
		return R.ok(relationService.getProductIdListByGroupId(id));
	}

	@Operation(summary = "【后台】分组关联商品")
	@PostMapping("/relation/product")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_relationProduct')")
	public R saveRelation(@Validated(Update.class) @RequestBody ProductGroupRelationDTO productGroupRelationDTO) {
		if (relationService.saveRelation(productGroupRelationDTO.getProductIds(), productGroupRelationDTO.getGroupId())) {
			return R.ok();
		}
		return R.fail();
	}

	@Operation(summary = "【后台】商品关联分组")
	@PostMapping("/relation/group")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_saveRelationByGroup')")
	public R saveRelationByGroup(@Validated(Save.class) @RequestBody ProductGroupRelationDTO productGroupRelationDTO) {
		if (relationService.saveRelation(productGroupRelationDTO.getProductIds(), productGroupRelationDTO.getGroupIds())) {
			return R.ok();
		}
		return R.fail();
	}


	@Operation(summary = "【后台】移除关联商品")
	@Parameter(name = "id", description = "主键id", required = true)
	@DeleteMapping("/relation/{id}")
	@PreAuthorize("@pms.hasPermission('admin_product_productGroup_deleteRelation')")
	public R deleteRelation(@PathVariable Long id) {
		if (relationService.deleteById(id)) {
			return R.ok();
		}
		return R.fail();
	}

}
