/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.experimental.UtilityClass;

/**
 * 购物车cookie工具类
 *
 * @author legendshop
 */
@UtilityClass
public class CartUtil {

	private final String LOGIN_CART = "login_cart";

	private final String UN_LOGIN_CART = "un_login_cart";

	/**
	 * 获取bean的名称
	 *
	 * @param userId
	 * @return
	 */
	public String getBeanName(Long userId) {
		if (ObjectUtil.isNull(userId)) {
			return UN_LOGIN_CART;
		} else {
			return LOGIN_CART;
		}
	}
}
