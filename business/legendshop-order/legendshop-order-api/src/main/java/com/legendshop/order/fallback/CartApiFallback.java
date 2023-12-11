/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.fallback;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.CartApi;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
public class CartApiFallback implements CartApi {

	@Override
	public R<Void> mergeCart(Long userId, String userKey) {
		return R.ok();
	}

}
