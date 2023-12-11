/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车相关接口
 *
 * @author legendshop
 * @create: 2021-09-26 13:46
 */
@RestController
public class CartController {

	@Autowired
	private CartService cartService;


	public R<Void> mergeCart(@RequestParam("userId") Long userId, @RequestParam("userKey") String userKey) {
		return cartService.mergeCart(userId, userKey);
	}
}
