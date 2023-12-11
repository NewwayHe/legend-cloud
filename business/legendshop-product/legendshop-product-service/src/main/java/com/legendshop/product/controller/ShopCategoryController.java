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
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dto.ShopCategoryTree;
import com.legendshop.product.service.ShopCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author legendshop
 */
@RestController
@Slf4j
@RequestMapping("/shopCategory")
@Api(tags = "类目管理")
public class ShopCategoryController {

	@Autowired
	private ShopCategoryService shopCategoryService;

	/**
	 * 获取店铺的所有类目,状态 1上线，2下线，3全部
	 *
	 * @param status
	 * @return
	 */
	@GetMapping("/getByShopIdAndStatus")
	@ApiOperation(value = "获取店铺的所有类目", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "status", value = "状态 1上线，2下线，3全部", dataType = "Long", required = true),
			@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺id", dataType = "Long", required = true)
	})
	public R<List<ShopCategoryTree>> getByShopIdAndStatus(@RequestParam("shopId") @NotNull(message = "店铺id不能为空") Long shopId, @RequestParam("status") @NotNull(message = "状态不能为空") Integer status) {
		// 获取符合条件的类目
		Set<ShopCategoryBO> all = new HashSet<>();
		all.addAll(shopCategoryService.queryByShopId(shopId, status));
		return R.ok(shopCategoryService.filterShopCategory(all));
	}

}
