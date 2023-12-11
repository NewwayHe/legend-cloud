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
import com.legendshop.product.dto.AccusationTypeDTO;
import com.legendshop.product.query.AccusationTypeQuery;
import com.legendshop.product.service.AccusationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品举报类型管理
 *
 * @author legendshop
 */
@Tag(name = "商品举报类型管理")
@RestController
@RequestMapping(value = "/admin/accusationType", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminAccusationTypeController {

	@Autowired
	private AccusationTypeService accusationTypeService;

	/**
	 * 保存
	 *
	 * @param d
	 * @return
	 */
	@PostMapping
	@SystemLog("保存商品举报类型")
	@Operation(summary = "【后台】保存商品举报类型")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_add')")
	public R save(@Valid @RequestBody AccusationTypeDTO d) {
		return accusationTypeService.save(d);
	}

	/**
	 * 更新
	 *
	 * @param d
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商品举报类型")
	@Operation(summary = "【后台】更新商品举报类型")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_update')")
	public R<Boolean> update(@Valid @RequestBody AccusationTypeDTO d) {
		return R.ok(accusationTypeService.update(d));
	}

	/**
	 * 根据id获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "举报类型ID", required = true)
	@Operation(summary = "【后台】根据id获取")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_get')")
	public R getById(@PathVariable Long id) {
		return R.ok(accusationTypeService.getById(id));
	}

	/**
	 * 根据id商品删除举报类型
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@Parameter(name = "id", description = "举报类型ID", required = true)
	@SystemLog("根据id商品删除举报类型")
	@Operation(summary = "【后台】根据id商品删除举报类型")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_del')")
	public R<Boolean> deleteById(@PathVariable Long id) {
		return accusationTypeService.deleteAccusationType(id);
	}

	/**
	 * 查询所有在线的举报类型
	 *
	 * @return
	 */
	@GetMapping("/all")
	@Operation(summary = "【后台】查询所有在线的举报类型")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_listAllOnLine')")
	public R<List<AccusationTypeDTO>> queryAllOnLine() {
		return R.ok(accusationTypeService.queryAllOnLine());
	}


	/**
	 * 批量更新状态
	 *
	 * @param accusationTypeDTO
	 * @return
	 */
	@PutMapping("/batchUpdateStatus")
	@SystemLog("商品举报类型批量更新状态")
	@Operation(summary = "【后台】商品举报类型批量更新状态")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_batchUpdateStatus')")
	public R batchUpdateStatus(@RequestBody AccusationTypeDTO accusationTypeDTO) {
		return accusationTypeService.batchUpdateStatus(accusationTypeDTO.getIds(), accusationTypeDTO.getStatus());
	}

	/**
	 * 批量删除
	 *
	 * @param accusationTypeDTO
	 * @return
	 */
	@DeleteMapping("/batchDelete")
	@SystemLog("商品举报类型批量删除")
	@Operation(summary = "【后台】商品举报类型批量删除")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_batchDelete')")
	public R batchDelete(@RequestBody AccusationTypeDTO accusationTypeDTO) {
		return accusationTypeService.batchDelete(accusationTypeDTO.getIds());
	}

	/**
	 * 商品举报类型分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【后台】商品举报类型分页查询")
	@PreAuthorize("@pms.hasPermission('admin_product_accusationType_page')")
	public R<PageSupport<AccusationTypeDTO>> page(AccusationTypeQuery query) {
		return R.ok(accusationTypeService.queryPage(query));
	}

}
