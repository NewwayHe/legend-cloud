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
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ParamGroupDTO;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ParamGroupQuery;
import com.legendshop.product.service.ParamGroupService;
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
 * 商品参数组管理【可搜索】
 *
 * @author legendshop
 */
@Tag(name = "商品参数组管理")
@RestController
@RequestMapping(value = "/admin/paramGroup", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminParamGroupController {

	@Autowired
	private ParamGroupService paramGroupService;

	/**
	 * 根据id查询参数组
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "参数组ID", required = true)
	@Operation(summary = "【后台】根据id查询参数组")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_get')")
	public R<ParamGroupBO> getById(@PathVariable("id") Long id) {
		return R.ok(paramGroupService.getById(id));
	}

	/**
	 * 根据id分页查询参数属性和属性值
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/param")
	@Operation(summary = "【后台】根据id分页查询参数属性和属性值")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_getParamAndValueById')")
	public R<PageSupport<ProductPropertyBO>> getParamAndValueById(ParamGroupQuery query) {
		return R.ok(paramGroupService.getParamAndValueById(query));
	}

	/**
	 * 根据id删除参数组
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除参数组")
	@Parameter(name = "id", description = "参数组ID", required = true)
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_del')")
	@Operation(summary = "【后台】根据id删除参数组")
	public R<String> delById(@PathVariable("id") Long id) {
		int result = paramGroupService.deleteParamGroupById(id);
		if (result > 0) {
			return R.ok("删除" + id + "成功");
		}
		return R.fail("删除" + id + "失败");

	}

	/**
	 * 保存商品参数组
	 *
	 * @param paramGroupDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存商品参数组")
	@Operation(summary = "【后台】保存商品参数组")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_add')")
	public R<String> save(@Valid @RequestBody ParamGroupDTO paramGroupDTO) {
		paramGroupDTO.setSource(ProductPropertySourceEnum.SYSTEM.value());
		Long result = paramGroupService.saveParamGroup(paramGroupDTO);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("保存失败");
	}

	/**
	 * 更新商品参数组
	 *
	 * @param paramGroupDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商品参数组")
	@Operation(summary = "【后台】更新商品参数组")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_update')")
	public R<String> update(@Valid @RequestBody ParamGroupDTO paramGroupDTO) {
		if (paramGroupService.updateParamGroup(paramGroupDTO) > 0) {
			return R.ok();
		}
		return R.fail("更新失败");
	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_page')")
	@Operation(summary = "【后台】分页查询")
	public R<PageSupport<ParamGroupBO>> page(ParamGroupQuery query) {
		query.setSource(ProductPropertySourceEnum.SYSTEM.value());
		return R.ok(paramGroupService.getParamGroupPage(query));
	}

	/**
	 * 查询所有在线的参数组
	 *
	 * @return
	 */
	@PostMapping("/allOnline")
	@Operation(summary = "【后台】查询所有在线的参数组admin_product_paramGroup_allOnline")
	@PreAuthorize("@pms.hasPermission('admin_product_paramGroup_allOnline')")
	public R<List<ParamGroupBO>> allOnline() {
		return R.ok(paramGroupService.allOnline());
	}
}
