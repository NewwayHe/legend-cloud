/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.ShopIncomingDTO;
import com.legendshop.pay.service.ShopIncomingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 进件接口
 *
 * @author legendshop
 * @create: 2021-03-26 10:47
 */
@RestController
@RequestMapping(value = "/shopIncoming", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopIncomingController {

	@Autowired
	private ShopIncomingService shopIncomingService;

	@PostMapping("/getByShopId")
	public R<ShopIncomingDTO> getByShopId(@RequestParam("shopId") Long shopId) {
		return shopIncomingService.getByShopId(shopId);
	}

}
