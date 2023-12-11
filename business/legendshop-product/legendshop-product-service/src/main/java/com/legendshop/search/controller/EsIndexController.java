/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.controller;

import com.legendshop.search.service.SearchCouponIndexService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 索引控制层
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "索引控制")
public class EsIndexController {

	private final SearchCouponIndexService searchCouponIndexService;

	/**
	 * 删除所有优惠卷索引
	 *
	 * @return
	 */

	@PostMapping("/deleteAllCoupon")
	public Boolean deleteAllCouponIndexByCouponId() {
		return searchCouponIndexService.delAllCouponIndex();
	}
}
