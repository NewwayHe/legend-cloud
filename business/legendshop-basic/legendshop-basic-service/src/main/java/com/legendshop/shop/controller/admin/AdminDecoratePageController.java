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
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.DecoratePageBO;
import com.legendshop.shop.dto.DecoratePageDTO;
import com.legendshop.shop.service.DecoratePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 装修页面(DecoratePage)控制层
 *
 * @author legendshop
 */
@Tag(name = "装修页面")
@RestController
@RequestMapping(value = "/admin/decoratePage", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDecoratePageController {


	@Autowired
	private DecoratePageService decoratePageService;

	@PreAuthorize("@pms.hasPermission('admin_decoratePage_page')")
	@Operation(summary = "【后台】获取装修页面分页列表")
	@GetMapping("/list")
	public R<PageSupport<DecoratePageBO>> list(DecoratePageQuery decoratePageQuery) {
		PageSupport<DecoratePageBO> result = decoratePageService.queryPageListDesc(decoratePageQuery);
		return R.ok(result);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_save')")
	@Operation(summary = "【后台】保存装修")
	@PostMapping("/save")
	public R<Long> save(@Valid @RequestBody DecoratePageDTO decoratePageDTO) {
		return decoratePageService.save(decoratePageDTO);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_release')")
	@Operation(summary = "【后台】发布装修")
	@PutMapping("/release")
	public R<Long> release(@Valid @RequestBody DecoratePageDTO decoratePageDTO) {
		return decoratePageService.release(decoratePageDTO);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_release')")
	@Operation(summary = "【后台】编辑页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/edit")
	public R<DecoratePageDTO> edit(@RequestParam Long id) {
		return decoratePageService.edit(id);
	}


	@Operation(summary = "【后台】查看页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/show")
	public R<DecoratePageDTO> show(@RequestParam Long id) {
		return decoratePageService.edit(id);
	}

	@PreAuthorize("@pms.hasPermission('admin_decoratePage_releasePage')")
	@Operation(summary = "【后台】发布页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/releasePage")
	public R releasePage(@RequestParam Long id) {
		return decoratePageService.releasePage(id);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_use')")
	@Operation(summary = "【后台】设为首页")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "category", description = "页面ID", required = false)
	})
	@PostMapping("/use")
	public R use(@RequestParam Long id, @RequestParam(value = "category", required = false, defaultValue = "INDEX") String category) {
		return decoratePageService.usePage(id, category);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_clone')")
	@Operation(summary = "【后台】复制页面")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "newName", description = "新页面名称", required = true)
	})
	@PostMapping("/clone")
	public R clone(@RequestParam Long id, @RequestParam String newName) {
		return decoratePageService.clone(id, newName);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_delete')")
	@Operation(summary = "【后台】删除页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@DeleteMapping("/{id}")
	public R delete(@PathVariable Long id) {
		return decoratePageService.deleteById(id);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_updateName')")
	@Operation(summary = "【后台】修改页面名称")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "newName", description = "新页面名称", required = true)
	})
	@PostMapping("/updateName")
	public R updateName(@RequestParam Long id, @RequestParam String newName) {
		return decoratePageService.updateName(id, newName);
	}


	@PreAuthorize("@pms.hasPermission('admin_decoratePage_updatePicture')")
	@Operation(summary = "【后台】修改封面图")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "coverPicture", description = "封面图", required = true)
	})
	@PostMapping("/updateCoverPicture")
	public R updateCoverPicture(@RequestParam Long id, @RequestParam String coverPicture) {
		return decoratePageService.updateCoverPicture(id, coverPicture);
	}


}
