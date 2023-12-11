/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.shop.dto.AdvSortImgDTO;
import com.legendshop.shop.query.AdvSortImgQuery;
import com.legendshop.shop.service.AdvSortImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (AdvSortImg)表控制层
 *
 * @author legendshop
 * @since 2021-07-09 15:28:05
 */
@Tag(name = "类目广告图片管理")
@RestController
@RequestMapping(value = "/admin/catAdvert", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminAdvSortImgController {

	/**
	 * (AdvSortImg)服务对象
	 */
	private final AdvSortImgService advSortImgService;

	/**
	 * 根据id删除广告图片
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除广告图片")
	@PreAuthorize("@pms.hasPermission('admin_product_AdvSortImg_del')")
	@Operation(summary = "【后台】根据id删除广告图片")
	public R deleteById(@PathVariable Long id) {
		return advSortImgService.deleteCategoryAdv(id);
	}

	/**
	 * 保存广告图片
	 *
	 * @param advSortImgDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存广告图片")
	@PreAuthorize("@pms.hasPermission('admin_product_AdvSortImg_add')")
	@Operation(summary = "【后台】保存广告图片")
	public R saveImg(@Valid @RequestBody AdvSortImgDTO advSortImgDTO) {
		return advSortImgService.saveCategoryAdv(advSortImgDTO);
	}

	/**
	 * 更新
	 *
	 * @param advSortImgDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新广告图片")
	@PreAuthorize("@pms.hasPermission('admin_product_AdvSortImg_update')")
	@Operation(summary = "【后台】更新广告图片")
	public R update(@Valid @RequestBody AdvSortImgDTO advSortImgDTO) {
		return advSortImgService.updateCategoryAdv(advSortImgDTO);
	}

	@PutMapping("/batch/status")
	@Operation(summary = "【后台】批量更新广告状态")
	public R updateStatus(@RequestBody List<Long> ids, @RequestParam Integer status) {
		advSortImgService.batchUpdateStatus(ids, status);
		return R.ok();
	}

	@GetMapping
	@Operation(summary = "【后台】查询分类广告分页")
	public R<PageSupport<AdvSortImgDTO>> queryPage(AdvSortImgQuery advSortImgQuery) {
		return advSortImgService.queryPage(advSortImgQuery);
	}

	/**
	 * 保存界面显示分类列表
	 *
	 * @return
	 */
	@GetMapping("/queryCategory")
	@SystemLog("保存界面显示分类列表")
	@PreAuthorize("@pms.hasPermission('s_product_AdvSortImg_query')")
	@Operation(summary = "【后台】保存界面显示分类列表")
	public R<List<CategoryBO>> queryCategory() {
		return advSortImgService.getTopCategory();
	}


	/**
	 * 根据分类id查询广告图片
	 *
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/queryByAdvSortId")
	@SystemLog("根据分类id查询广告图片")
	@PreAuthorize("@pms.hasPermission('s_product_AdvSortImg_update')")
	@Operation(summary = "【后台】根据分类id查询广告图片")
	public R queryByAdvSortId(@PathVariable("sortId") Long categoryId) {
		return R.ok(advSortImgService.queryByAdvSortId(categoryId));
	}
}
