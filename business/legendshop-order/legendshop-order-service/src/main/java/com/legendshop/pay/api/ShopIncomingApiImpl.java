/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.ShopIncomingDTO;
import com.legendshop.pay.service.ShopIncomingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class ShopIncomingApiImpl implements ShopIncomingApi {

	private final ShopIncomingService shopIncomingService;

	@Override
	public R<ShopIncomingDTO> getByShopId(@RequestParam("shopId") Long shopId) {
		return shopIncomingService.getByShopId(shopId);
	}

}
