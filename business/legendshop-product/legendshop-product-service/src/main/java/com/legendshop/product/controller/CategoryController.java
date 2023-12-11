/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/category")
@Tag(name = "类目管理")
public class CategoryController {

	final CategoryService categoryService;

	/**
	 * 根据父级id查询类目
	 *
	 * @param parentId
	 * @return
	 */
	@GetMapping("/queryListByParentId")
	@Operation(summary = "【公共】根据父级id查询类目")
	@Parameter(name = "parentId", description = "父级id", required = true)
	public R<List<CategoryBO>> queryListByParentId(Long parentId, String name) {
		/*	status 上线状态【0：下线，1：上线，2：全部】 */
		List<CategoryBO> categoryBOS = categoryService.queryByParentIdAndName(parentId, name, 1);
		return R.ok(categoryBOS);
	}
}
