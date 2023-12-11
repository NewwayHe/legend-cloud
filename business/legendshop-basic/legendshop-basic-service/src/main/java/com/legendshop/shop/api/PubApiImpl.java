/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.service.PubService;
import lombok.AllArgsConstructor;
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
public class PubApiImpl implements PubApi {

	private final PubService pubService;

	@Override
	public R<Integer> userUnreadMsg(@RequestParam("userId") Long userId) {
		return pubService.userUnreadMsg(userId);
	}
}
