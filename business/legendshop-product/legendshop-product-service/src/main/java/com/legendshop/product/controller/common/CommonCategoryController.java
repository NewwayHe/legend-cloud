/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.common;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页商品分类
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "用户端商品分类")
public class CommonCategoryController {

	@Autowired
	private CategoryService categoryService;


	/**
	 * 首页商品分类带上children
	 *
	 * @param parentId
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【用户】首页商品分类")
	@SystemLog("\"【用户】首页商品分类")
	public R<List<CategoryTree>> getCategory(Long parentId) {
		Assert.notNull(parentId, "parentId不能为空");
		return categoryService.getTreeById(parentId);
	}
}
