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
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.service.NewsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帮助栏目控制器
 *
 * @author legendshop
 */
@Tag(name = "帮助栏目")
@RestController
@RequestMapping(value = "/admin/news/category", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminNewsCategoryController {

	@Autowired
	private NewsCategoryService newsCategoryService;

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_page')")
	@Operation(summary = "【后台】帮助栏目分页查询")
	@GetMapping("/page")
	public R<PageSupport<NewsCategoryDTO>> page(NewsCategoryQuery newsCategoryQuery) {
		PageSupport<NewsCategoryDTO> pageSupport = newsCategoryService.page(newsCategoryQuery);
		return R.ok(pageSupport);
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_updateStatus')")
	@Operation(summary = "【后台】修改帮助栏目状态")
	@PutMapping("/updateStatus")
	public R<String> updateStatus(@RequestBody NewsCategoryDTO newsCategoryDTO) {
		if (newsCategoryService.updateStatus(newsCategoryDTO.getStatus(), newsCategoryDTO.getId()) > 0) {
			return R.ok();
		}
		return R.fail("修改状态失败");
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_deleteById')")
	@Operation(summary = "【后台】删除帮助栏目")
	@Parameter(name = "id", description = "栏目id", required = true)
	@DeleteMapping("/{id}")
	public R<Boolean> deleteById(@PathVariable Long id) {
		if (newsCategoryService.deleteNewsFalg(id)) {
			return R.ok();
		}
		return R.fail("该栏目下有帮助新闻，不能删除!");
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_add')")
	@Operation(summary = "【后台】新增帮助栏目")
	@PostMapping
	public R<String> save(@Valid @RequestBody NewsCategoryDTO newsCategoryDTO) {
		this.newsCategoryService.save(newsCategoryDTO);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_update')")
	@Operation(summary = "【后台】修改帮助栏目")
	@PutMapping
	public R<Boolean> update(@Valid @RequestBody NewsCategoryDTO newsCategoryDTO) {
		if (newsCategoryService.update(newsCategoryDTO)) {
			return R.ok();
		}
		return R.fail("修改失败");
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_list')")
	@Operation(summary = "【后台】查询所有的帮助栏目")
	@GetMapping("/query")
	public R<List<NewsCategoryDTO>> query() {
		List<NewsCategoryDTO> categoryDtoList = newsCategoryService.getNewsCategoryList();
		return R.ok(categoryDtoList);
	}

	@PreAuthorize("@pms.hasPermission('admin_newsCategory_queryByDisplayPage')")
	@Operation(summary = "【后台】根据显示页面端查询帮助栏目")
	@Parameter(name = "displayPage", description = "显示页面端", required = true)
	@GetMapping("/displayPage")
	public R<List<NewsCategoryDTO>> queryByDisplayPage(@RequestParam Integer displayPage) {
		return R.ok(newsCategoryService.queryByDisplayPage(displayPage));
	}

}
