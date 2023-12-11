/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.bo.NewsBO;
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.query.NewsQuery;
import com.legendshop.shop.service.NewsCategoryService;
import com.legendshop.shop.service.NewsService;
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
 * 帮助文章控制器
 *
 * @author legendshop
 */
@Tag(name = "帮助文章")
@RestController
@RequestMapping(value = "/admin/news", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminNewsController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsCategoryService newsCategoryService;

	@PreAuthorize("@pms.hasPermission('admin_news_page')")
	@Operation(summary = "【后台】帮助文章的分页查询test")
	@GetMapping("/page")
	public R<PageSupport<NewsBO>> page(NewsQuery newsQuery) {

		PageSupport<NewsBO> pageSupport = newsService.page(newsQuery);
		return R.ok(pageSupport);
	}

	@PreAuthorize("@pms.hasPermission('admin_news_updateStatus')")
	@Operation(summary = "【后台】修改帮助文章状态")
	@Parameters({
			@Parameter(name = "id", description = "文章id", required = true),
			@Parameter(name = "status", description = "状态", required = true)
	})
	@PostMapping("/updateStatus")
	public R<String> updateStatus(Long id, Integer status) {
		if (newsService.updateStatus(status, id) > 0) {
			return R.ok();
		}
		return R.fail("修改失败");
	}

	@PreAuthorize("@pms.hasPermission('admin_news_add')")
	@Operation(summary = "【后台】新增帮助文章")
	@PostMapping
	public R<String> save(@Valid @RequestBody NewsDTO newsDTO) {
		Long userId = SecurityUtils.getUserId();
		newsDTO.setUserId(userId);
		if (ObjectUtil.isNotEmpty(newsDTO.getNewsCategoryId())) {
			R<NewsCategoryDTO> newsCategoryDtoR = newsCategoryService.checkDisplayPage(newsDTO);
			if (!newsCategoryDtoR.getSuccess()) {
				return R.fail(newsCategoryDtoR.getMsg());
			}
		}
		newsService.save(newsDTO);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('admin_news_update')")
	@Operation(summary = "【后台】修改帮助文章")
	@PutMapping
	public R<Boolean> update(@Valid @RequestBody NewsDTO newsDTO) {
		if (ObjectUtil.isNotEmpty(newsDTO.getNewsCategoryId())) {
			R<NewsCategoryDTO> newsCategoryDtoR = newsCategoryService.checkDisplayPage(newsDTO);
			if (!newsCategoryDtoR.getSuccess()) {
				return R.fail(newsCategoryDtoR.getMsg());
			}
		}
		if (newsService.update(newsDTO)) {
			return R.ok();
		}
		return R.fail("修改失败");
	}

	@PreAuthorize("@pms.hasPermission('admin_news_deleteById')")
	@Operation(summary = "【后台】删除帮助文章")
	@Parameter(name = "id", description = "文章id", required = true)
	@DeleteMapping("/{id}")
	public R<Boolean> deleteById(@PathVariable Long id) {
		if (newsService.deleteById(id)) {
			return R.ok();
		}
		return R.fail("删除失败");
	}


	@PreAuthorize("@pms.hasPermission('admin_news_getNewsAndDisPlay')")
	@Operation(summary = "【后台】根据id查询帮助文章")
	@Parameter(name = "id", description = "文章id", required = true)
	@GetMapping("/get")
	public R<NewsDTO> getNewsAndDisPlay(@RequestParam Long id) {
		return R.ok(newsService.getNewsAndDisPlay(id));
	}

}
