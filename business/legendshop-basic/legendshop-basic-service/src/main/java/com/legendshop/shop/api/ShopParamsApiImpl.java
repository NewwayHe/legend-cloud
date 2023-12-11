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
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.service.ShopParamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class ShopParamsApiImpl implements ShopParamsApi {

	final ShopParamsService shopParamsService;

	@Override
	public <T> R<T> getConfigDtoByParamName(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "name") String name, @RequestBody Class<T> clazz) {
		T configDtoByParamName = this.shopParamsService.getConfigDtoByParamName(shopId, name, clazz);
		return R.ok(configDtoByParamName);
	}

	@Override
	public R<List<ShopParamItemDTO>> getSysParamItemsByParamName(@RequestParam(value = "name") String name, @RequestParam(value = "shopId") Long shopId) {
		return R.ok(this.shopParamsService.getShopParamItemsByParamName(name, shopId));
	}
}
