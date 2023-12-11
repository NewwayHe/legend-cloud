/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class CartApiImpl implements CartApi {

	private final CartService cartService;

	@Override
	public R<Void> mergeCart(Long userId, String userKey) {
		return cartService.mergeCart(userId, userKey);
	}
}
