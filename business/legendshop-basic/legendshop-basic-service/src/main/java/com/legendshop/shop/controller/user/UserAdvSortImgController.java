/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.AdvSortImgDTO;
import com.legendshop.shop.service.AdvSortImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "类目广告用户端")
@RestController
@RequestMapping(value = "/p/catAdvert", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserAdvSortImgController {

	@Autowired
	private AdvSortImgService advSortImgService;

	@GetMapping
	@Operation(summary = "【用户】根据分类id查询广告列表")
	public R<List<AdvSortImgDTO>> queryByCategoryId(Long categoryId) {
		return advSortImgService.queryByCategoryId(categoryId);
	}

}
