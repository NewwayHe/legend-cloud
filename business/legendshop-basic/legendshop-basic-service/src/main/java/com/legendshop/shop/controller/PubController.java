/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.service.PubService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告
 *
 * @author legendshop
 * @create: 2021-06-16 18:31
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/pub", produces = MediaType.APPLICATION_JSON_VALUE)
public class PubController {

	private final PubService pubService;

	@GetMapping("/userUnreadMsg")
	public R<Integer> userUnreadMsg(@RequestParam("userId") Long userId) {
		return pubService.userUnreadMsg(userId);
	}

}
