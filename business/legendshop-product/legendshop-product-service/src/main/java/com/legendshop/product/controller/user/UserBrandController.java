/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "品牌管理")
@RestController
@RequestMapping(value = "/brand", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserBrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * 	查询所有的在线品牌
	 */
	@GetMapping("/all")
	@Operation(summary = "【通用】查询所有在线的品牌")
	@Parameter(name = "brandName", description = "品牌名称", required = true)
	public R<List<BrandBO>> queryAllOnline(String brandName) {
		return R.ok(brandService.getAllOnline(0L, brandName));
	}


	/**
	 *	根据id查询品牌
	 */
	@GetMapping("/p/{id}")
	@Operation(summary = "【通用】根据id查询品牌")
	@Parameter(name = "id", description = "品牌ID", required = true)
	public R<BrandBO> getById(@PathVariable("id") Long id) {
		return R.ok(brandService.getById(id));
	}
}

